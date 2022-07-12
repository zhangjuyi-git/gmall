package com.atguigu.gmall.order.mq;

import org.springframework.stereotype.Component;

/**
 * @Author:juyi
 * @Date:2022/7/11 18:57
 */
@Component
public class HeHeQUeueListener {


//    @RabbitListener(queues = "hehe")
//    public void Listener(Message message, Channel channel) throws IOException {
//
//        MessageProperties properties = message.getMessageProperties();
//
//        byte[] body = message.getBody();
//
//        System.out.println("收到消息:  " + new String(body));
//
//        try {
//            channel.basicAck(properties.getDeliveryTag(),false);
//        } catch (IOException e) {
//            channel.basicNack(properties.getDeliveryTag(),false,true);
//        }
//
//    }

}
