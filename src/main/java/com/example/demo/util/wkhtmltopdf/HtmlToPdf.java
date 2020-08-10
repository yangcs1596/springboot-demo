package com.example.demo.util.wkhtmltopdf;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;

@Log4j2
public class HtmlToPdf {
    private static String wkhtmltopdfPath = "D:\\wkhtmltopdf\\bin\\wkhtmltopdf";

    /**
     * html转pdf
     *
     * @param htmlStr  html代码
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String htmlStr, String destPath) {
        String srcPath =  string2File(htmlStr);
        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(wkhtmltopdfPath);
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);
        boolean result = true;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        } catch (Exception e) {
            result = false;
           log.error(ExceptionUtils.getStackTrace(e));
        } finally {
            new File(srcPath).delete();
            proc.destroy();
        }

        return result;
    }

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
     *
     * @param res            原字符串
     * @return 成功标记
     */
    public static String string2File(String res) {
        String htmlFilePath = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
//            String newFileName = SerialGenerator.generateFileName(null, "html");
//            String filePath = CommonPath.generalFileRelativePath(newFileName);
            htmlFilePath = "C:\\\\Users\\\\Administrator\\\\Desktop\\\\测试文件.html";
            File distFile = new File(htmlFilePath);
            if (!distFile.getParentFile().exists()) {
                distFile.getParentFile().mkdirs();
            }
            bufferedReader = new BufferedReader(new StringReader(res));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024];
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return htmlFilePath;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return htmlFilePath;
    }
    public static void main(String[] args) {
        HtmlToPdf.convert("<html><head></head><body><img src=\"data:image/png;base64,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\"></body></html>", "E:/baidu.pdf");
    }
}
