package com.example.demo.util.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Author: 杨春生
 * @Date: 2019/11/29 17:04
 * @Description:
 */
public class PDF2IMG {
    /**
     * 转换全部的pdf
     *
     * @param fileAddress 文件地址
     * @param filename    PDF文件名
     * @param type        图片类型
     */
    public static void pdf2png(String fileAddress, String filename, String type) {
        // 将pdf装图片 并且自定义图片得格式大小
        File file = new File(fileAddress + "\\" + filename + ".pdf");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                //经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
                BufferedImage image = renderer.renderImageWithDPI(i, 96); // Windows native DPI
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, type, new File(fileAddress + "\\" + filename + "_" + (i + 1) + "." + type));
            }
            //关闭文件,不然该pdf文件会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 自由确定起始页和终止页
     *
     * @param fileAddress  文件地址
     * @param filename     pdf文件名
     * @param indexOfStart 开始页  开始转换的页码，从0开始
     * @param indexOfEnd   结束页  停止转换的页码，-1为全部
     * @param type         图片类型
     */
    public static void pdf2png(String fileAddress, String filename, int indexOfStart, int indexOfEnd, String type) {
        // 将pdf装图片 并且自定义图片得格式大小
        File file = new File(fileAddress + "\\" + filename + ".pdf");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = indexOfStart; i < indexOfEnd; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, type, new File(fileAddress + "\\" + filename + "_" + (i + 1) + "." + type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pdf2Word(String pdfFile) throws Exception {
        PDDocument doc = PDDocument.load(new File(pdfFile));
        //获取总页数
        int pagenumber = doc.getNumberOfPages();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pdfFile.substring(0, pdfFile.indexOf(".")) + ".doc");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Writer writer = null;
        try {
            //文件按字节读取，然后按照UTF-8的格式编码显示
            writer = new OutputStreamWriter(fos, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //生成PDF文档内容剥离器
        PDFTextStripper stripper = new PDFTextStripper();
        //排序
        stripper.setSortByPosition(true);
        //设置转换的开始页
        stripper.setStartPage(1);
        //设置转换的结束页
        stripper.setEndPage(pagenumber);
        try {
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String fileName = "转123";
        String pdfPath = "C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹";
        pdf2png(pdfPath, fileName, "jpg");

    }
}
