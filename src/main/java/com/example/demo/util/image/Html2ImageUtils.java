package com.example.demo.util.image;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.w3c.dom.Document;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @Author: 杨春生
 * @Date: 2020/9/5 12:30
 * @Description:
 */
public class Html2ImageUtils {
    public static void main(String[] args) {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        String htmlstr = "<table width='654' cellpadding='0' cellspacing='0' bordercolor='#FFFFFF'><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr></table>";
        imageGenerator.loadHtml(htmlstr);
//        imageGenerator.getBufferedImage();
        imageGenerator.saveAsImage("hello2.png");
//        imageGenerator.saveAsHtmlWithMap("D:\\hello.html", "D:\\hello.png");

        //    loadUrl(url) - Loads HTML from URL object or URL string. (从url载入html)
        //    loadHtml(html) - Loads HTML source. (载入本地html)
        //
        //    saveAsImage(file) - Save loaded HTML as image. (以图片形式保存html)
        //    saveAsHtmlWithMap(file, imageUrl) -  (创建一个HTML文件包含客户端image-map)
        //
        //    getLinks() - List all links in the HTML document and their corresponding href, target, title, position and dimension. (列出所有在HTML文档的链接和相应href、目标、头衔、位置和尺寸)
        //    getBufferedImage() - Get AWT buffered image of the HTML. (获得awt,html缓冲后的图片)
        //    getLinksMapMarkup(mapName) - Get HTML snippet of the client-side image-map <map> generated from the links. (HTML代码段里获得的客户端image-map <地图>产生的链接)
        //
        //    get/setOrientation(orientation) - Get/Set document orientation (left-to-right or right-to-left). (get/set文本定位)
        //    get/setSize(dimension) - Get/Set size of the generated image. (设置生成图片大小)
    }

    private static String getTemplate(String template, Map<String,Object> map) throws IOException, freemarker.template.TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
        String templatePath = Html2ImageUtils.class.getResource("/").getPath()+"/templates";
        cfg.setDirectoryForTemplateLoading(new File(templatePath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        Template temp = cfg.getTemplate(template);
        StringWriter stringWriter = new StringWriter();
        temp.process(map, stringWriter);
        stringWriter.flush();
        stringWriter.close();
        String resutl = stringWriter.getBuffer().toString();
        return resutl;
    }

    public static void turnImage(String template, Map<String,Object> map, HttpServletResponse response) throws Exception {
        String html = getTemplate(template, map);

        byte[] bytes=html.getBytes();
        ByteArrayInputStream bin=new ByteArrayInputStream(bytes);
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document document=builder.parse(bin);
        //设置宽高
        Java2DRenderer renderer = new Java2DRenderer(document,250,300);
        BufferedImage img = renderer.getImage();
        response.setContentType("image/jpeg");
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        ImageIO.write(img, "jpg", response.getOutputStream());
    }
}
