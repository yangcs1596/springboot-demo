//package com.example.demo.util.aspose;
//
//import com.aspose.words.Document;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//
///**
// * @Author: 杨春生
// * @Date: 2020/3/26 10:20
// * @Description:
// */
//public class AsposeTest {
//
//
//    public static boolean getLicense() {
//        boolean result = false;
//        try {
//            InputStream is = AsposeTest.class.getClassLoader().getResourceAsStream("./static/license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
//            License aposeLic = new License();
//            aposeLic.setLicense(is);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public static void doc2pdf(String inPath, String outPath) {
//        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
//            return;
//        }
//        try {
//            long old = System.currentTimeMillis();
//            File file = new File(outPath); // 新建一个空白pdf文档
//            FileOutputStream os = new FileOutputStream(file);
//            Document doc = new Document(inPath); // Address是将要被转化的word文档
//            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
//            // EPUB, XPS, SWF 相互转换
//            long now = System.currentTimeMillis();
//            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        doc2pdf("C:\\\\Users\\\\Administrator\\\\Desktop\\\\2020-07-10 11_20_27-商标转让声明公证（企业）.docx",
//                "C:\\\\Users\\\\Administrator\\\\Desktop\\\\2020-07-10 11_20_27-商标转让声明公证（企业）.pdf");
//
//        //这种转换只使用与windows或装了word工具
////            File inputWord = new File("C:\\Users\\Administrator\\Desktop\\2020-07-08 11_00_13-商标转让声明公证（个人）.docx");
////            File outputFile = new File("C:\\Users\\Administrator\\Desktop\\哈哈哈.pdf");
////            try  {
////                InputStream docxInputStream = new FileInputStream(inputWord);
////                OutputStream outputStream = new FileOutputStream(outputFile);
////                IConverter converter = LocalConverter.builder().build();
////                converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
////                outputStream.close();
////                System.out.println("success");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//    }
//}
