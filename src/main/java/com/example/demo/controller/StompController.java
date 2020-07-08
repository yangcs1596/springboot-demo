package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.FileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 杨春生
 * @Date: 2019/11/12 11:36
 * @Description:
 */
@Slf4j
@Controller
public class StompController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 功能描述: 实现的方式一
     *
     * @Payload 所以抽象一下，payload 可以理解为一系列信息中最为关键的信息。
     * 对于程序员来说就是在程序中 起关键作用的代码。
     * 回到代码中，举一个最简单的例子，一个 ajax 请求返回一个 JSON 格式
     * {
     * status: 200,
     * hasError: false,
     * data: {
     * userId: 1,
     * name: 'undefined'
     * }
     * }
     * 复制代码
     * 这里的 data 就是 payload，也就是关键信息。而 status、hasError等信息是load，虽然也是信息，但相对没有那么重要。
     */
    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message) {
        return message;
    }

    /**
     * 功能描述: 实现的方式二
     */
    @MessageMapping("/news2")
    public void broadcastNews2(@Payload FileEntity fileEntity, String ksg) {
//        JSONObject obj = JSON.parseObject(message);
        this.simpMessagingTemplate.convertAndSend("/exchange/topicExchange/news" + fileEntity.getId(), fileEntity);
    }

    /**
     * 功能描述: 订阅时的消息
     */
    @SubscribeMapping("/marco/{id}")
    public String handleSubscription(@DestinationVariable("id") String id) {
        System.out.println("this is the @SubscribeMapping('/marco')" + id);
        String str = JSON.toJSONString("{'message':'订阅消息的响应'}");
        return str;
    }

    /**
     * 功能描述: 订阅时的消息
     */
    @SubscribeMapping("/marco2/*")
    public String handleSubscription2() {
        String str = JSON.toJSONString("{'message':'订阅消息的响应2'}");
        return str;
    }

    /**
     * 功能描述：处理消息异常
     */
    @MessageExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void processMethod(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("抛异常了！" + ex.getLocalizedMessage());
        log.error("抛异常了！" + ex.getLocalizedMessage());
        response.getWriter().printf(ex.getMessage());
        response.flushBuffer();
    }


}
