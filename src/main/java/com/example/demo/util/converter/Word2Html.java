package com.example.demo.util.converter;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author: 杨春生
 * @Date: 2020/1/8 09:07
 * @Description:
 */
@Slf4j
public class Word2Html {
    /**
     * 编码格式错误的问题
     *
     * @param path
     * @param content
     * @param encoding
     * @throws IOException
     */
    public static void write(String path, String content, String encoding)
            throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), encoding));
        writer.write(content);
        writer.close();
    }

    /**
     * 按格式读取文件内容
     *
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String read(String path, String encoding) throws IOException {
        String content = "";
        File file = new File(path);
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), encoding));
        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }
        reader.close();
        return content;
    }


    private static OpenOfficeConnection startOpenOffice() {
        //OpenOffice的安装目录，linux环境下需要手动启动openoffice服务
        String OpenOffice_HOME = "D:\\OpenOffice\\OpenOffice4\\";
        // 启动OpenOffice的服务
        String command = OpenOffice_HOME + "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("启动openoffice服务异常！");
        }
        //创建连接
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        return connection;
    }

    /**
     * 功能描述:  doc转为html
     */
    private static void doc2Html(String sourcePath, String targetPath, String fielExt) {
        if(sourcePath == targetPath){
            return ;
        }
        File inputFile = new File(sourcePath);
        File outputFile = new File(targetPath);
        OpenOfficeConnection connection = startOpenOffice();
        try {
            connection.connect();
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
            DocumentFormat txt = formatReg.getFormatByFileExtension("doc");
            DocumentFormat pdf = formatReg.getFormatByFileExtension(fielExt);
            converter.convert(inputFile, txt, outputFile, pdf);
            //解决可能乱码的问题
            //write(targetPath, read(targetPath, "GBK"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     *  windows默认编码是GBK，而linux默认编码是utf8
     *
     *  所以此处的转换和读取问题不应该。
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String docPath = "C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\转换乱码.docx";
        String htmlath = "C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\转换乱码.html";
        doc2Html(docPath, htmlath, "html");
        //测试二
//       File file = new File("D:\\\\测试文件夹\\\\三类技能证书提交要求.html");
////       Document doc = Jsoup.parse(file ,"GBK");
////        Elements media = doc.select("[src]");
////        for (Element src : media) {
////            if (src.tagName().equals("img")) {
////                String link = src.attr("src");
////                System.out.println(link);
////                src.attr("src","/889/"+link);
////            }
////        }
////       System.out.println(doc.select("body").toString());
//        System.out.println(FileUtil.mainName("三类技能证书提交要求.html"));
//        System.out.println(CaptchaUtil.createLineCaptcha(50,50));
//        System.out.println();
        //message could be found


    }
}
