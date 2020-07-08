package com.example.demo.util.POI;

import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * 功能描述：利用POI实现word转成pdf文件
 * @Author: 杨春生
 * @Date: 2020/2/26 17:40
 * @Description:
 */
public class POITest {
    //这个方法利用的是本地的office转换，不适用于linux
//    public static void main(String[] args) {
//
//        File inputWord = new File("C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\页数变多.doc");
//        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\页数变多222.pdf");
//        try  {
//            InputStream docxInputStream = new FileInputStream(inputWord);
//            OutputStream outputStream = new FileOutputStream(outputFile);
//            //本地调用
////            IConverter converter = LocalConverter.builder()//
////                    .baseFolder(new File("C:\Users\documents4j\temp"));
////                    .workerPool(20, 25, 2, TimeUnit.SECONDS)
////                    .processTimeout(5, TimeUnit.SECONDS)
////                    .build();
//            IConverter converter = LocalConverter.builder().build();
//            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
//            outputStream.close();
//            System.out.println("success");
//            converter.kill();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws Exception {
        docx2Pdf();
    }

    /**
     * 功能： 将docx转为pdf
     */
    public static void docx2Pdf() throws Exception{
        String docPath = "C:\\Users\\Administrator\\Desktop\\2020-07-08 11_00_13-商标转让声明公证（个人）.docx";
        String pdfPath = "C:\\Users\\Administrator\\Desktop\\2020-07-08 11_00_13-商标转让声明公证（个人）.pdf";

        XWPFDocument document;
        InputStream doc = new FileInputStream(docPath);
        document = new XWPFDocument(doc);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(pdfPath);
        PdfConverter.getInstance().convert(document, out, options);

        doc.close();
        out.close();
    }
    /**
     * 功能描述： DOCX转换成html
     * @return
     * @throws Exception
     */
    public static String docxToHtml() throws Exception {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String imagePath = path.getAbsolutePath() + "\\static\\image";
        String sourceFileName = path.getAbsolutePath() + "\\static\\test\\123.docx";
        String targetFileName = path.getAbsolutePath() + "\\static\\test\\123.html";

        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(imagePath)));
            // html中图片的路径
            options.URIResolver(new BasicURIResolver("image"));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
        return targetFileName;
    }

//    public static void main(String[] args) throws Exception{
//        String filepath = "C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\页数变多.doc";
//        String outpath = "C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\转换乱码2.pdf";
//
//        InputStream source = new FileInputStream(filepath);
//        OutputStream target = new FileOutputStream(outpath);
//        Map<String, String> params = new HashMap<String, String>();
//
//
//        PdfOptions options = PdfOptions.create();
//
//        wordConverterToPdf(source, target, options, params);
//        System.exit(0);
//    }
//    /**
//     * 将word文档， 转换成pdf, 中间替换掉变量
//     * @param source 源为word文档， 必须为docx文档
//     * @param target 目标输出
//     * @param params 需要替换的变量
//     * @throws Exception
//     */
//    public static void wordConverterToPdf(InputStream source,
//                                          OutputStream target, Map<String, String> params) throws Exception {
//        wordConverterToPdf(source, target, null, params);
//    }
//    /**
//     * 将word文档， 转换成pdf, 中间替换掉变量
//     * @param source 源为word文档， 必须为docx文档
//     * @param target 目标输出
//     * @param params 需要替换的变量
//     * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 或者其他
//     * @throws Exception
//     */
//    public static void wordConverterToPdf(InputStream source, OutputStream target,
//                                          PdfOptions options,
//                                          Map<String, String> params) throws Exception {
//        XWPFDocument doc = new XWPFDocument(source);
//        paragraphReplace(doc.getParagraphs(), params);
//        for (XWPFTable table : doc.getTables()) {
//            for (XWPFTableRow row : table.getRows()) {
//                for (XWPFTableCell cell : row.getTableCells()) {
//                    paragraphReplace(cell.getParagraphs(), params);
//                }
//            }
//        }
//        PdfConverter.getInstance().convert(doc, target, options);
//    }
//
//    /** 替换段落中内容 */
//    private static void paragraphReplace(List<XWPFParagraph> paragraphs, Map<String, String> params) {
//        if (MapUtils.isNotEmpty(params)) {
//            for (XWPFParagraph p : paragraphs){
//                for (XWPFRun r : p.getRuns()){
//                    String content = r.getText(r.getTextPosition());
//                    /*                    logger.info(content);*/
//                    if(StringUtils.isNotEmpty(content) && params.containsKey(content)) {
//                        r.setText(params.get(content), 0);
//                    }
//                }
//            }
//        }
//    }

}
