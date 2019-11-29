package com.lypgod.demo.rabbitmq._3_fair;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Log4j2
public class FairDispatcherSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private Queue fairDispatcherQueue;

    private static int index = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = ("FairDispatcher Message " + index++);
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(fairDispatcherQueue.getName(), message);
    }
}
