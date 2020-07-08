package com.example.demo;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Author: 杨春生
 * @Date: 2019/11/25 17:02
 * @Description:
 */
public class TestMain {
    /**
     * java在pdf模板的指定位置插入图片
     * 注意是表单模式的pdf ？
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final Integer PDF_RIGHT_DOWN_POINT_X = 595;
        // 模板文件路径
        String templatePath = "D:\\测试文件夹\\sign2.pdf";
        // 生成的文件路径
        String targetPath = "D:\\测试文件夹\\签名.pdf";
        // 书签名
        String fieldName = "签名";
        // 图片路径
        String imagePath = "D:\\测试文件夹\\picture0.jpg";

        // 读取模板文件
        InputStream input = new FileInputStream(new File(templatePath));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
        // 提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        // 通过域名获取所在页和坐标，左下角为起点
        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        Rectangle signRect = form.getFieldPositions(fieldName).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();

        // 读图片
        Image image = Image.getInstance(imagePath);
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(pageNo);
        // 根据域的大小缩放图片
        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        // 添加图片
        image.setAbsolutePosition(x, y);
        under.addImage(image);


        stamper.close();
        reader.close();
    }

}
