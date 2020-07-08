package com.example.demo.util.converter.html;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 需要引入的jar包
 *         <dependency>
 *             <groupId>net.sf.cssbox</groupId>
 *             <artifactId>pdf2dom</artifactId>
 *             <version>1.7</version> 1.9最新
 *         </dependency>
 *         <dependency>
 *             <groupId>org.apache.pdfbox</groupId>
 *             <artifactId>pdfbox</artifactId>
 *             <version>2.0.12</version>  2.0.18最新
 *         </dependency>
 *         <dependency>
 *             <groupId>org.apache.pdfbox</groupId>
 *             <artifactId>pdfbox-tools</artifactId>
 *             <version>2.0.12</version>
 *         </dependency>
 */

public class PDF2Html {

    /**
     * 功能描述: 将文件转换为byte数组
     */
    private static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 读取文件内容？
     * @param document
     * @throws IOException
     */
    public static void readText(PDDocument document) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        System.out.println(text);
    }

    /**
     *  这个方法是将pdf转成图片后，再将图片写到html里面
     *  调用例子： PdfToImage("D:\\测试文件夹\\三类技能证书提交要求 - 副本.pdf");
     * @param pdfurl
     */
    public static void PdfToImage(String pdfurl){
        StringBuffer buffer = new StringBuffer();
        FileOutputStream fos;
        PDDocument document;
        File pdfFile;
        int size;
        BufferedImage image;
        FileOutputStream out;
        Long randStr = 0l;
        //PDF转换成HTML保存的文件夹
        String path = "E:\\Xshell\\1";
        File htmlsDir = new File(path);
        if(!htmlsDir.exists()){
            htmlsDir.mkdirs();
        }
        File htmlDir = new File(path+"/");
        if(!htmlDir.exists()){
            htmlDir.mkdirs();
        }
        try{
            //遍历处理pdf附件
            randStr = System.currentTimeMillis();
            buffer.append("<!doctype html>\r\n");
            buffer.append("<head>\r\n");
            buffer.append("<meta charset=\"UTF-8\">\r\n");
            buffer.append("</head>\r\n");
            buffer.append("<body style=\"background-color:gray;\">\r\n");
            buffer.append("<style>\r\n");
            buffer.append("img {background-color:#fff; text-align:center; width:100%; max-width:100%;margin-top:6px;}\r\n");
            buffer.append("</style>\r\n");
            document = new PDDocument();
            //pdf附件
            pdfFile = new File(pdfurl);
            document = PDDocument.load(pdfFile, (String) null);
            size = document.getNumberOfPages();
            Long start = System.currentTimeMillis(), end = null;
            System.out.println("===>pdf : " + pdfFile.getName() +" , size : " + size);
            PDFRenderer reader = new PDFRenderer(document);
            for(int i=0 ; i < size; i++){
                //image = new PDFRenderer(document).renderImageWithDPI(i,130,ImageType.RGB);
                image = reader.renderImage(i, 1.5f);
                //生成图片,保存位置
                out = new FileOutputStream(path + "/"+ "image" + "_" + i + ".jpg");
                ImageIO.write(image, "png", out); //使用png的清晰度
                //将图片路径追加到网页文件里
                buffer.append("<img src=\"" + path +"/"+ "image" + "_" + i + ".jpg\"/>\r\n");
                image = null; out.flush(); out.close();
            }
            reader = null;
            document.close();
            buffer.append("</body>\r\n");
            buffer.append("</html>");
            end = System.currentTimeMillis() - start;
            System.out.println("===> Reading pdf times: " + (end/1000));
            start = end = null;
            //生成网页文件
            fos = new FileOutputStream(path+randStr+".html");
            System.out.println(path+randStr+".html");
            fos.write(buffer.toString().getBytes());
            fos.flush(); fos.close();
            buffer.setLength(0);

        }catch(Exception e){
            System.out.println("===>Reader parse pdf to jpg error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * pdf转换html
     * 这种方式会将html里面的图片转换成了base64编码，图片大小不好控制
     * @param args
     */
    public static void main(String[] args) throws Exception {
         String outputPath = "C:\\Users\\Administrator\\Desktop\\二维码\\123.html";
//         System.out.println(Jsoup.parse(FileUtils.readFileToString(new File(outputPath), "utf-8")));
        // byte[] bytes =  getBytes("D:\\测试文件夹\\三类技能证书提交要求.pdf");
//        try() 写在()里面会自动关闭流
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath)),"UTF-8"));){
            //加载PDF文档
            byte[] bytes = FileUtils.readFileToByteArray(new File("C:\\Users\\Administrator\\Desktop\\二维码\\123.pdf"));
            PDDocument document = PDDocument.load(bytes);
            // 输出pdf文本
           //  readText(document);
            PDFDomTree pdfDomTree = new PDFDomTree();
            pdfDomTree.writeText(document,out);
            document.close();
            System.out.println(FileUtils.readFileToString(new File(outputPath), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
