package com.example.demo.controller;

import com.example.demo.entity.UserDTO;
import com.example.demo.message.iRabbitMqSend;
import com.example.demo.util.image.Html2ImageUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private iRabbitMqSend iRabbitMqSend;

    @ApiOperation(value="校验参数", notes="测试接口")
    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    @ResponseBody
    public String test(UserDTO userDTO) {
       return "1234";
    }

    @ApiOperation(value="校验参数", notes="测试接口")
    @RequestMapping(value = {"/test2"}, method = RequestMethod.GET)
    @ResponseBody
    public void test2(HttpServletResponse response) {
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("msg","2020 年 09 月 05 日");
            map.put("img","http://localhost:8082/file/downLoad");
            Html2ImageUtils.turnImage("index.ftl",map,response);
        } catch (Exception e) {
            log.error("异常", e.getMessage());
        }
    }

}
