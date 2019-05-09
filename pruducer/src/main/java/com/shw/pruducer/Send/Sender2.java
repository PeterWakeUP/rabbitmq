package com.shw.pruducer.Send;

import com.shw.common.DTO.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender2{

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(Order order)throws Exception{
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        //发送消息
        rabbitTemplate.convertAndSend("order-exchange",
                "order.abcd",
                order,     //发送内容
                correlationData);   //发送唯一标识

        //确认模式
        rabbitTemplate.setConfirmCallback((correlationData1, ack, cause) -> {
            if (!ack) {
                //try to resend msg
            } else {
                //delete msg in db
            }
        });

        //未投递到 queue 退回模式
        rabbitTemplate.setReturnCallback((Message, i, s1, s2, s3) -> {
            //TODO
        });
    }






}
