package com.example.demo.controller.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Author: 杨春生
 * @Date: 2020/9/5 15:00
 * @Description:
 */
@RestController
@ResponseBody
@Slf4j
@RequestMapping("file")
public class FileController {
    @GetMapping("downLoad")
    public void downLoad(HttpServletResponse response, @RequestParam(defaultValue = "C:\\Users\\Administrator\\Desktop\\公证处.png") String path){
        File file = new File(path);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            InputStream in = new FileInputStream(file);
            StreamUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
