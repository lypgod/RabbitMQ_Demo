package com.lypgod.demo.rabbitmq._9_dlx;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static com.lypgod.demo.rabbitmq.util.R00_General_Config.TIME_FORMATTER;
import static com.lypgod.demo.rabbitmq.util.R09_Dlx_Config.ROUTING_KEY_DLX_BUSINESS;

@Log4j2
public class DlxQueueSender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private DirectExchange businessExchange;

    @Scheduled(fixedDelay = 10000, initialDelay = 500)
    public void send() {
        String message = "DLX Message @ " + TIME_FORMATTER.format(LocalDateTime.now());
        log.warn("message sent ==> [{}]", message);
        this.rabbitTemplate.convertAndSend(businessExchange.getName(), ROUTING_KEY_DLX_BUSINESS, message);
    }
}
