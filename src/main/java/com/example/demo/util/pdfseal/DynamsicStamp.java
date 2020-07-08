//package com.example.demo.util.pdfseal;
//
//import com.spire.pdf.PdfDocument;
//import com.spire.pdf.PdfPageBase;
//import com.spire.pdf.annotations.PdfRubberStampAnnotation;
//import com.spire.pdf.annotations.appearance.PdfAppearance;
//import com.spire.pdf.graphics.*;
//
//import java.awt.*;
//import java.awt.geom.Point2D;
//import java.awt.geom.Rectangle2D;
//import java.text.SimpleDateFormat;
//
///**
// * 功能描述:  动态印章
// * @auther: 杨春生
// * @date: 2019/11/29 11:56
// */
//public class DynamsicStamp {
//
//    public static void main(String[] args) {
//
//        //创建PdfDocument对象
//        PdfDocument document = new PdfDocument();
//
//        //加载PDF文档
//        document.loadFromFile("test.pdf");
//
//        //获取第3页
//        PdfPageBase page = document.getPages().get(2);
//
//        //创建PdfTamplate对象
//        PdfTemplate template = new PdfTemplate(185, 50);
//
//        //创建两种字体
//        PdfTrueTypeFont font1 = new PdfTrueTypeFont(new Font("Arial Unicode MS", Font.PLAIN  ,15), true);
//        PdfTrueTypeFont font2 = new PdfTrueTypeFont(new Font("Arial Unicode MS", Font.PLAIN  ,10), true);
//
//        //创建画刷
//        PdfSolidBrush solidBrush = new PdfSolidBrush(new PdfRGBColor(Color.blue));
//        Rectangle2D rect1 = new Rectangle2D.Float();
//        rect1.setFrame(new Point2D.Float(0,0),template.getSize());
//
//        //创建圆角矩形路径
//        int CornerRadius = 20;
//        PdfPath path = new PdfPath();
//        path.addArc(template.getBounds().getX(), template.getBounds().getY(), CornerRadius, CornerRadius, 180, 90);
//        path.addArc(template.getBounds().getX() + template.getWidth() - CornerRadius,template.getBounds().getY(), CornerRadius, CornerRadius, 270, 90);
//        path.addArc(template.getBounds().getX() + template.getWidth() - CornerRadius, template.getBounds().getY()+ template.getHeight() - CornerRadius, CornerRadius, CornerRadius, 0, 90);
//        path.addArc(template.getBounds().getX(), template.getBounds().getY() + template.getHeight() - CornerRadius, CornerRadius, CornerRadius, 90, 90);
//        path.addLine( template.getBounds().getX(), template.getBounds().getY() + template.getHeight() - CornerRadius, template.getBounds().getX(), template.getBounds().getY() + CornerRadius / 2);
//
//        //绘制路径到模板，并进行填充
//        template.getGraphics().drawPath(PdfPens.getBlue(), path);
//
//        //在模板上绘制文字及动态日期
//        String s1 = "已审核\n";
//        String s2 = "社区管理中心 " + dateToString(new java.util.Date(),"yyyy-MM-dd HH:mm:ss");
//        template.getGraphics().drawString(s1, font1, solidBrush, new Point2D.Float(5, 5));
//        template.getGraphics().drawString(s2, font2, solidBrush, new Point2D.Float(5, 28));
//
//        //创建PdfRubberStampAnnotation对象，并指定其位置和大小
//        Rectangle2D rect2= new Rectangle2D.Float();
//        rect2.setFrame(new Point2D.Float((float)(page.getActualSize().getWidth()-250),(float)(page.getActualSize().getHeight()-150)),  template.getSize());
//        PdfRubberStampAnnotation stamp = new PdfRubberStampAnnotation(rect2);
//
//        //创建PdfAppearance对象，应用模板为一般状态
//        PdfAppearance appearance = new PdfAppearance(stamp);
//        appearance.setNormal(template);
//
//        //应用样式到图章
//        stamp.setAppearance(appearance);
//
//        //添加图章到Annotation集合
//        page.getAnnotationsWidget().add(stamp);
//
//        //保存文档
//        document.saveToFile("DynamicStamp.pdf");
//        document.close();
//    }
//
//    //将日期转化成字符串
//    public static String dateToString(java.util.Date poDate,String pcFormat) {
//        SimpleDateFormat loFormat = new SimpleDateFormat(pcFormat);
//        return loFormat.format(poDate);
//    }
//}
