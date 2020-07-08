package com.example.demo.util.converter.html;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;

/**
 * @Author: 杨春生
 * @Date: 2020/1/8 10:04
 * @Description:
 */
@Component
public class ReadHtmlUtil {
    /**
     * 解析html文件
     *
     * @param file
     * @return
     */
    public static String readHtml(File file, String charsetName) {
        String body = "";
        try {
            FileInputStream iStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(iStream,charsetName);
            BufferedReader htmlReader = new BufferedReader(reader);

            String line;
            boolean found = false;
            while (!found && (line = htmlReader.readLine()) != null) {
                // 在<body>的前面可能存在空格
                if (line.toLowerCase().indexOf("<body") != -1) {
                    found = true;
                }
            }

            found = false;
            while (!found && (line = htmlReader.readLine()) != null) {
                if (line.toLowerCase().indexOf("</body") != -1) {
                    found = true;
                } else {
                    // 如果存在图片，则将相对路径转换为绝对路径
                    String lowerCaseLine = line.toLowerCase();
                    if (lowerCaseLine.contains("src")) {

                        //这里是定义图片的访问路径
                        String directory =  file.getParent() + System.getProperty("file.separator");
                        // 如果路径名不以反斜杠结尾，则手动添加反斜杠
                        /*if (!directory.endsWith("\\")) {
                            directory = directory + "\\";
                        }*/
                        //    line = line.substring(0,  lowerCaseLine.indexOf("src") + 5) + directory + line.substring(lowerCaseLine.indexOf("src") + 5);
                        /*String filename = extractFilename(line);
                        line = line.substring(0,  lowerCaseLine.indexOf("src") + 5) + directory + filename + line.substring(line.indexOf(filename) + filename.length());
                    */
                        // 如果该行存在多个<img>元素，则分行进行替代
                        // <img后带一个或多个空格
                        String[] splitLines = lowerCaseLine.split("<img\\s+");
                        // 因为java中引用的问题不能使用for each
                        for (int i = 0; i < splitLines.length; i++) {
                            if (splitLines[i].trim().toLowerCase().startsWith("src")) {
                                splitLines[i] = splitLines[i].substring(0, splitLines[i].toLowerCase().indexOf("src") + 5)
                                        + directory
                                        + URLDecoder.decode(splitLines[i].substring(splitLines[i].toLowerCase().indexOf("src") + 5),"UTF-8");
                            }
                        }

                        // 最后进行拼接
                        line = "";
                        // 循环次数要-1，因为最后一个字符串后不需要添加<img
                        for (int i = 0; i < splitLines.length - 1; i++) {
                            line = line + splitLines[i] + "<img ";
                        }
                        line = line + splitLines[splitLines.length - 1];
                    }

                    body = body + line + "\n";
                }
            }
            htmlReader.close();
            //        System.out.println(body);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * @param htmlLine 一行html片段，包含<img>元素
     * @return 文件名
     */
    public static String extractFilename(String htmlLine) {
        int srcIndex = htmlLine.toLowerCase().indexOf("src=");
        // 图片不存在，返回空字符串
        if (srcIndex == -1) {
            return "";
        } else {
            String htmlSrc = htmlLine.substring(srcIndex + 4);
            // 默认为双引号，但也有可能为单引号
            char splitChar = '\"';
            if (htmlSrc.charAt(0) == '\'') {
                splitChar = '\'';
            }
            String[] firstSplit = htmlSrc.split(String.valueOf(splitChar));
            // 第0位为空字符串
            String path = firstSplit[1];
            // 匹配正斜杠或反斜杠
            String[] secondSplit = path.split("[/\\\\]");
            return secondSplit[secondSplit.length - 1];
        }
    }

    /**
     * 读取文件内容
     * @param filePathAndName
     * @return
     */
    public static String readFile(String filePathAndName) {
        String fileContent = "";
        try {
            File f = new File(filePathAndName);
            if(f.isFile()&&f.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    //将读取到的字符拼接
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        System.out.println("fileContent:"+fileContent);
        return fileContent;
    }
}
