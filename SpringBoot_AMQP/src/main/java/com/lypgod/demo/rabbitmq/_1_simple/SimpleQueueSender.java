package com.lypgod.demo.rabbitmq._1_simple;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.lypgod.demo.rabbitmq.util.RabbitMqConfig.TIME_FORMATTER;

@Log4j2
public class SimpleQueueSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private Queue simpleQueue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Simple Message@" + TIME_FORMATTER.format(LocalDateTime.now());
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(simpleQueue.getName(), message);
    }
}
