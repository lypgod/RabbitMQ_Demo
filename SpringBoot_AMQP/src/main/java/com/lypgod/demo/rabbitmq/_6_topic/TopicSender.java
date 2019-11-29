package com.lypgod.demo.rabbitmq._6_topic;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Random;

@Log4j2
public class TopicSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private TopicExchange topicExchange;

    private static int index = 0;
    private final String[] keys = {"quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox",
            "lazy.brown.fox", "lazy.pink.rabbit", "quick.brown.fox"};

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        int random = new Random().nextInt(keys.length);
        String message = ("Topic Message " + index++ + " [" + keys[random] + "]");
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(topicExchange.getName(), keys[random], message);
    }
}
