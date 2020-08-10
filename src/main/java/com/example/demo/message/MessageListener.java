package com.example.demo.message;

import com.example.demo.common.amp.QueueConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 杨春生
 * @Date: 2020/7/22 09:57
 * @Description:
 */
@Component
@RabbitListener(queues = QueueConstant.TestMsg)
public class MessageListener {
    /**
     * 业务实现
     */
    @RabbitHandler
    public void process(Object object, Channel channel, Message message) throws Exception {
        System.out.println("Receiver:" + object);
        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
