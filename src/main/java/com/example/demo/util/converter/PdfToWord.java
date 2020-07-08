package com.example.demo.util.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @Author: 杨春生
 * @Date: 2020/1/8 09:05
 * @Description:
 */
public class PdfToWord {
    /**
     * 代码所用到的jar包 pdfbox-1.8.2.jar 另行下载
     * @param args
     */
    public static void main(String[] args){
        try{
            String pdfFile = "D:\\测试文件夹\\空空.pdf";
            String docFile = "D:\\测试文件夹\\空空.doc";
            PDDocument doc = PDDocument.load(new File(pdfFile));
            int pagenumber = doc.getNumberOfPages();
            File file = new File(docFile);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(docFile);
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            PDFTextStripper stripper = new PDFTextStripper();
            // 排序
            stripper.setSortByPosition(true);
            // 设置转换的开始页
            stripper.setStartPage(1);
            // 设置转换的结束页
            stripper.setEndPage(pagenumber);
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
            System.out.println("pdf转换word成功！");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
