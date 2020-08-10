package com.example.demo.util.wkhtmltopdf;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Log4j2
public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is){
        this.is = is;
    }

    @Override
    public void run(){
        InputStreamReader isr = null ;
        try{
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
            }
        }catch (IOException e){
            log.error("PDF 转换异常{}", ExceptionUtils.getStackTrace(e));
        }finally{
            try {
                isr.close();
                is.close();
            } catch (IOException e) {
                log.error("关闭流异常", ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
