package com.duan.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@AllArgsConstructor
@Configuration
public class MqConfig {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct //次注解是在springboot项目初始化之前执行的方法
    public void init(){
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("触发return callback,");
            log.error("exchange: {}", returned.getExchange());
            log.error("routingKey: {}", returned.getRoutingKey());
            log.error("message: {}", returned.getMessage());
            log.error("replyCode: {}", returned.getReplyCode());
            log.error("replyText: {}", returned.getReplyText());
        });
    }
}
