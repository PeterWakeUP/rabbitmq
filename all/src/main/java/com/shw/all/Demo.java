package com.shw.all;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class Demo {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 生产者
     */
    @RequestMapping("/publish")
    public void publish() {
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration("4000");//在这里也可以设置超时时间,也可以设置x-message-ttl
            return message;
        };
        this.rabbitTemplate.convertAndSend("liveExchange", "info", "一条消息", postProcessor);
    }


    /**
     * 消费者
     */
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "liveQueue", arguments =
                    {@Argument(name = "x-dead-letter-exchange", value = "deadExchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "deadKey"),
                            @Argument(name = "x-message-ttl", value = "2000", type = "java.lang.Integer"),
                            @Argument(name = "x-max-length",value = "5",type = "java.lang.Integer")//队列最大长度
                    }),//可以指定多种属性
                    exchange = @Exchange(value = "liveExchange"),
                    key = {"info", "error", "warning"}
            )
    })
    public void subscribe(@Headers Map<String, Object> headers, Channel channel, String message) throws Exception {
        long consumerTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("收到消息");
        System.out.println(message);
        //channel.basicAck(consumerTag, false);//消息确认
        //channel.basicNack(consumerTag, false, false);//拒绝消息，让消息进入死信交换机
    }


    /**
     * 死信消费者
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "deadQueue"),
                    exchange = @Exchange(value = "deadExchange"),
                    key = "deadKey"
            )
    })
    public void deadSubscribe(@Headers Map<String, Object> headers, Channel channel, String message) throws IOException {
        System.out.println("死信:" + message);
        long consumerTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //channel.basicAck(consumerTag, true);
        //channel.basicNack(consumerTag, false, false);//拒绝消息
    }
}
