package com.example.demo.controller.media;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Author: 杨春生
 * @Date: 2019/12/23 10:40
 * @Description:
 */
@Controller
public class FileViewController {
    @RequestMapping("/video")
    public void mediaStream(HttpServletRequest request, HttpServletResponse response, String filPath) {
        File file = new File("/home");
    }
}
