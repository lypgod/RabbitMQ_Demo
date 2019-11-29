package com.lypgod.demo.rabbitmq._5_direct;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Random;

import static com.lypgod.demo.rabbitmq.util.RabbitMqConfig.*;

@Log4j2
public class DirectSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private DirectExchange directExchange;

    private static int index = 0;
    private final String[] keys = {ROUTING_KEY_INFO, ROUTING_KEY_WARN, ROUTING_KEY_ERROR};

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        int random = new Random().nextInt(3);
        String message = ("Direct Message " + index++ + " [" + keys[random] + "]");
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(directExchange.getName(), keys[random], message);
    }
}
