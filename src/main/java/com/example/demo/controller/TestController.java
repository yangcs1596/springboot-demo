package com.example.demo.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.demo.entity.UserDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 功能描述： 将json数据对象，变成了xml数据
 *
 * @Author: 杨春生
 * @Date: 2019/11/28 14:37
 * @Description:
 */
@Controller
@Slf4j
public class TestController {
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value = {"/test2"}, method = RequestMethod.GET)
    @ResponseBody
    public String saveXmlTest(HttpServletRequest request) {

        UserDTO user = new UserDTO();
        File file = new File("D://测试文件夹//hello.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(UserDTO.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            //格式化输出，即按标签自动换行，否则就是一行输出
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //设置编码（默认编码就是utf-8）
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //是否省略xml头信息，默认不省略（false）
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            user.setAge(123456);
            user.setFather("zzz");
            marshaller.marshal(user, file);
            return "success999";
        } catch (JAXBException e) {
            e.printStackTrace();
            return "failure";
        }
    }

    @ApiOperation(value="获取用户列表2", notes="")
    @RequestMapping(value = {"/test4"}, method = RequestMethod.GET)
    @ResponseBody
    public void test4(HttpServletRequest request, HttpServletResponse response) {

        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(150, 60);
        try {
            captcha.setBackground(Color.white);
            log.info(captcha.getCode());
            captcha.write(response.getOutputStream());
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
