package com.example.demo.config;

import com.example.demo.common.amp.QueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 杨春生
 * @Date: 2020/7/22 10:01
 * @Description:
 */
@Configuration
public class RabbitMqConfig {
    /**
     * Queue 可以有4个参数
     *      1.队列名
     *      2.durable       持久化消息队列 ,rabbitmq重启的时候不需要创建新的队列 默认true
     *      3.auto-delete   表示消息队列没有在使用时将被自动删除 默认是false
     *      4.exclusive     表示该消息队列是否只在当前connection生效,默认是false
     **/
    @Bean(name = "hello")
    public Queue queueHello(){
        return new Queue(QueueConstant.TestMsg);
    }
}
