package com.example.demo.util.converter;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.google.common.collect.Maps;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

/**
 * 功能描述：PDF转化为图片工具类
 *
 * @Author: 杨春生
 * @Date: 2019/12/2 14:57
 * @Description:
 */
public class PDFToImage {

    private static Logger LOGGER = LoggerFactory.getLogger(PDFToImage.class);

    /**
     * 转换全部的pdf
     *
     * @param pdfPath PDF文件名地址
     * @param type    图片类型
     */
    public static Map<String, Object> convert(String pdfPath, String type) {
        // 将pdf装图片 并且自定义图片得格式大小
        File file = new File(pdfPath);
        Map<String, Object> imgNames = Maps.newHashMapWithExpectedSize(1);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            String targetPath = pdfPath.substring(0, pdfPath.lastIndexOf("."));
            for (int i = 0; i < pageCount; i++) {
                //经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
                // Windows native DPI
                BufferedImage image = renderer.renderImageWithDPI(i, 96);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                String filePath = targetPath + "_" + (i + 1) + "." + type;
                ImageIO.write(image, type, new File(filePath));
                imgNames.put(String.valueOf(i + 1), filePath);
            }
            //关闭文件,不然该pdf文件可能会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("pdf转换图片异常");
        }
        return imgNames;
    }

    /**
     * pdf转为图片，转换格式默认png
     *
     * @param pdfPath
     * @return 生成图片的路径
     */
    public static Map<String, Object> pdf2png(String pdfPath) {
        return convert(pdfPath, "png");
    }

    /**
     * 功能描述: 将word转换为pdf文件
     * 使用了openoffice 的转换
     *
     * @param: docPath
     * @return: pdfPath
     * @auther: 杨春生
     * @date: 2019/12/2 16:19
     */
    public static String word2pdf(String docPath) {
        String pdfPath = docPath.substring(0, docPath.lastIndexOf(".")) + ".pdf";
        File inputFile = new File(docPath);
        File outputFile = new File(pdfPath);
        //连接openoffices
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
            DocumentFormat txt = formatReg.getFormatByFileExtension("odt");
            DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf");
            converter.convert(inputFile, txt, outputFile, pdf);
        } catch (ConnectException e) {
            LOGGER.info("word转换pdf文件转换出错", e);
        }
        return pdfPath;
    }
}
