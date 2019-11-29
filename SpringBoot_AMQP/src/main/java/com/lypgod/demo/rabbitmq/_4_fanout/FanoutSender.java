package com.lypgod.demo.rabbitmq._4_fanout;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Log4j2
public class FanoutSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private FanoutExchange fanoutExchange;

    private static int index = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = ("Fanout Message " + index++);
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", message);
    }
}
