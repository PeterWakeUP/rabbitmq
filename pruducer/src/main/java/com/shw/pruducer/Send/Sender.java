package com.shw.pruducer.Send;

import com.shw.common.DTO.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(Order order)throws Exception{
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange",
                "order.abcd",
                order,     //发送内容
                correlationData);   //发送唯一标识
    }

    //确认模式
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }

    //未投递到 queue 退回模式
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {

    }
}
