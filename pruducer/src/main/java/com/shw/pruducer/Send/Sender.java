package com.shw.pruducer.Send;

import com.shw.common.DTO.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(Order order)throws Exception{
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange",
                "order.abcd",
                order,
                correlationData);
    }
}
