package com.lypgod.demo.rabbitmq._8_ack;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static com.lypgod.demo.rabbitmq.util.R00_General_Config.TIME_FORMATTER;

@Log4j2
public class AckQueueSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private Queue ackQueue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Ack Message@" + TIME_FORMATTER.format(LocalDateTime.now());
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(ackQueue.getName(), message);
    }
}
