package com.lypgod.demo.rabbitmq._1_simple;

import com.lypgod.demo.rabbitmq.util.RabbitMqConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.lypgod.demo.rabbitmq.util.RabbitMqConfig.QUEUE_NAME_SIMPLE;

/**
 * @author lypgod
 */
@RabbitListener(queues = QUEUE_NAME_SIMPLE)
@Log4j2
public class SimpleQueueListener {
    @RabbitHandler
    public void receive(String message) {
        RabbitMqConfig.process(0, message);
    }
}