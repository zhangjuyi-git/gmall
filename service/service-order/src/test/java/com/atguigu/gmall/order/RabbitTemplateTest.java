package com.atguigu.gmall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author:juyi
 * @Date:2022/7/11 18:42
 */
@SpringBootTest
public class RabbitTemplateTest {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    void testSend(){
        rabbitTemplate.convertAndSend("haha","heihei","666666");
    }

}
