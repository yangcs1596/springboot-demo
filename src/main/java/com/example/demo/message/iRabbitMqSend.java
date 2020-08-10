package com.example.demo.message;

import com.example.demo.common.amp.QueueConstant;
import com.example.demo.entity.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 杨春生
 * @Date: 2020/7/22 10:05
 * @Description:
 */
@Component
@Slf4j
public class iRabbitMqSend implements iRabbitMq, RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback{
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendHello(UserDTO userDTO) {
        this.amqpTemplate.convertAndSend(QueueConstant.TestMsg,"我是发送消息的内容! ");
    }
    /**
     * 消息回调确认方法
     * 如果消息没有到exchange,则confirm回调,ack=false
     * 如果消息到达exchange,则confirm回调,ack=true
     * @param
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //logger.info("confirm--message:回调消息ID为: " + correlationData.getId());
        if (ack) {
            //logger.info("confirm--message:消息发送成功");
        } else {
            log.info("confirm--message:消息发送失败" + cause);
        }
    }

    /**
     * exchange到queue成功,则不回调return
     * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("return--message:" + new String(message.getBody()) + ",replyCode:" + replyCode
                + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }
}
