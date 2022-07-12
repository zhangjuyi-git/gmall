package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.constant.MqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:juyi
 * @Date:2022/7/11 19:21
 */

@Slf4j
@Component
public class MqConfiguration {

    //将队列 交换机 绑定关系放到Spring容器中
    @Bean
    RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory){

        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template,connectionFactory);

        template.setReturnCallback(((message, i, s, s1, s2) -> {
            log.info("ReturnCallBack: message:[{}]",message);
        }));

        //mq确认消息  回调
        template.setConfirmCallback((correlationData, b, s) -> {
            log.info("ConfirmCallback: ack:[{}]",b,s);
        });
        return template;
    }


    /**
     * 订单事件交换机
     *
     * @return {@link Exchange}
     */
    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange(MqConst.EXCHANGE_ORDER_EVENT,
                true,
                false);
    }


    /**
     * 订单的延迟队列，利用队列进行超时计时
     *
     * @return {@link Queue}
     */
    @Bean
    public Queue orderDelayQueue(){

        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",MqConst.ORDER_TIMEOUT);
        arguments.put("x-dead-letter-exchange",MqConst.EXCHANGE_ORDER_EVENT);
        arguments.put("x-dead-letter-routing-key",MqConst.RK_ORDER_TIMEOUT);
        return new Queue(
                MqConst.QUEUE_ORDER_DELAY,
                true,
                false,
                false,
                arguments
        );
    }


    /**
     * 订单交换机和延迟队列进行绑定
     * 订单一创建好，消息就先抵达延迟队列
     *
     * @return {@link Binding}
     */
    @Bean
    public Binding orderDelayQueueBinding(){
        return new Binding(
                MqConst.QUEUE_ORDER_DELAY,
                Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_CREATE,
                null
        );
    }


    /**
     * 订单死信队列。
     * 关单服务消费这个队列就能拿到所有待关闭的超时订单
     *
     * @return {@link Queue}
     */
    @Bean
    public Queue orderDeadQueue(){
        return new Queue(MqConst.QUEUE_ORDER_DEAD,true,false,false);
    }


    @Bean
    public Binding orderDeadQueueBinding(){
        return new Binding(
                MqConst.QUEUE_ORDER_DEAD,
                Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_TIMEOUT,
                null);
    }

}
