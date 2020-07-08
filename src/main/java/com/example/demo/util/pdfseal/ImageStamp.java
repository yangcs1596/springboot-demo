//package com.example.demo.util.pdfseal;
//
///**
// * 这里的工具类对 spire.pdf.free 2.2.2jar包引用非常不友好，建议玩一玩就好了
//<!--印章-->
//<!--<dependency>-->
//<!--<groupId>e-iceblue</groupId>-->
//<!--<artifactId>spire.pdf.free</artifactId>-->
//<!--<version>2.2.2</version>-->
//<!--</dependency>-->
//<!--印章结束-->
// * @Author: 杨春生
// * @Date: 2019/11/29 11:52
// * @Description:
// */
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;
//import com.spire.pdf.PdfPageBase;
//import com.spire.pdf.annotations.PdfRubberStampAnnotation;
//import com.spire.pdf.annotations.appearance.PdfAppearance;
//import com.spire.pdf.graphics.PdfImage;
//import com.spire.pdf.graphics.PdfTemplate;
//import lombok.extern.slf4j.Slf4j;
//
//import java.awt.geom.Rectangle2D;
//import java.util.UUID;
//
//@Slf4j
//public class ImageStamp {
//
//    public static void main(String[] args) {
//
//        //创建PdfDocument对象，加载PDF测试文档
//        PdfDocument doc = new PdfDocument();
//        doc.loadFromFile("D:\\测试文件夹\\签章\\test.pdf");
//
//        //获取文档第3页
//        PdfPageBase page = doc.getPages().get(0);
//
//        //加载印章图片
//        PdfImage image = PdfImage.fromFile("D:\\测试文件夹\\签章\\test.jpg");
//        //获取印章图片的宽度和高度
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        //创建PdfTemplate对象
//        PdfTemplate template = new PdfTemplate(width, height);
//        //将图片绘制到模板
//        template.getGraphics().drawImage(image, 0, 0, width, height);
//
//        //创建PdfRubebrStampAnnotation对象，指定大小和位置
//        Rectangle2D rect = new Rectangle2D.Float((float) (page.getActualSize().getWidth() - width - 10), (float) (page.getActualSize().getHeight() - height - 60), width, height);
//        PdfRubberStampAnnotation stamp = new PdfRubberStampAnnotation(rect);
//
//        //创建PdfAppearance对象
//        PdfAppearance pdfAppearance = new PdfAppearance(stamp);
//        //将模板应用为PdfAppearance的一般状态
//        pdfAppearance.setNormal(template);
//        //将PdfAppearance 应用为图章的样式
//        stamp.setAppearance(pdfAppearance);
//
//        //添加图章到PDF
//        page.getAnnotationsWidget().add(stamp);
//
//        String targetName= UUID.randomUUID()+".pdf";
//        log.info(targetName);
//        //保存文档
//        doc.saveToFile("D:\\测试文件夹\\签章\\" + targetName, FileFormat.PDF);
//
//    }
//}
