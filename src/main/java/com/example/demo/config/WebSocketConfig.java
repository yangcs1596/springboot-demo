package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Author: 杨春生
 * @Date: 2019/11/12 11:20
 * @Description: configureMessageBroker主要做两件事情：
 * <p>
 * 1、创建内存中的消息代理，其中包含一个或多个用于发送和接收消息的目标。定义了目标地址前缀：topic和queue。
 * 它们遵循以下惯例：通过pub-sub模型将以topic为前缀的消息传递到所有订阅客户端的目标地址。另一方面，私有消息的目标地址通常以queue为前缀。
 * 2、定义前缀app，用于过滤目标地址，这些地址在Controller中被@MessageMapping修饰的方法处理。
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String RelayHost;
    @Value("${spring.rabbitmq.username}")
    private String ClientLogin;
    @Value("${spring.rabbitmq.password}")
    private String ClientPasscode;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mywebsockets")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {//MessageBrokerRegistry
//        config.enableSimpleBroker("/topic/", "/queue/");
        config.enableStompBrokerRelay("/topic", "/queue", "/exchange")
//                .setRelayHost("192.168.88.94")
                .setRelayPort(61613)
                .setClientLogin(ClientLogin)
                .setClientPasscode(ClientPasscode)
                .setSystemLogin(ClientLogin)
                .setSystemPasscode(ClientPasscode);
        config.setApplicationDestinationPrefixes("/app");
    }

}
