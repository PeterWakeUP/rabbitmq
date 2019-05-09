package com.shw.pruducer;

import com.alibaba.fastjson.JSON;
import com.shw.common.DTO.Order;
import com.shw.pruducer.Send.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SendTest {

    private final Logger logger =  LoggerFactory.getLogger(getClass());

    @Autowired
    Sender sender;

    @Test
    public void test(){
        Order order = new Order();
        order.setId("1");
        order.setMessageId("1");
        order.setName("order 1");
        try {
            sender.send(order);
            logger.info(String.format("++++++++++++++++++++++++++++++++++++++++++++++++++====%s", JSON.toJSONString(order)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
