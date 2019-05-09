package com.shw.consumer.Listen;

import com.rabbitmq.client.Channel;
import com.shw.common.DTO.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class consume {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //注解自动创建交换机、队列、绑定routingkey
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-quene", durable = "true"),
            exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
            key = "order.*" //order.* == order.jkl,   order.# ==  order.jkl.jkl.jkl....
        )
    )
    @RabbitHandler
    public void receive(@Payload Order order, @Headers Map<String, Object> headers, Channel channel)throws Exception{
        //消费者操作
        logger.info("-----收到消息，开始操作----------");
        logger.info("订单ID   " + order.getId());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        //ack,手工确认签收，主动回mq broker一个请求, 第二个参数表示一次是否 ack 多条消息
        channel.basicAck(deliveryTag, false);
    }
}
