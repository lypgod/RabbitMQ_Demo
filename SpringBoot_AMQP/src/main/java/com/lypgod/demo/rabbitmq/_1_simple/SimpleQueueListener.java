package com.lypgod.demo.rabbitmq._1_simple;

import com.lypgod.demo.rabbitmq.util.R00_General_Config;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.lypgod.demo.rabbitmq.util.R01_Simple_Config.QUEUE_NAME_SIMPLE;

/**
 * @author lypgod
 */
@RabbitListener(queues = QUEUE_NAME_SIMPLE)
@Log4j2
public class SimpleQueueListener {
    @RabbitHandler
    public void receive(String message) {
        R00_General_Config.process(0, message);
    }
}