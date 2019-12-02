package com.lypgod.demo.rabbitmq._2_round_robin;

import com.lypgod.demo.rabbitmq.util.R00_General_Config;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.lypgod.demo.rabbitmq.util.R02_RoundRobin_Config.QUEUE_NAME_ROUND;

/**
 * @author lypgod
 */
@RabbitListener(queues = QUEUE_NAME_ROUND)
@Log4j2
public class RoundRobinListener {
    private final int instance;

    public RoundRobinListener(int i) {
        this.instance = i;
    }

    @RabbitHandler
    public void receive(String message) {
        R00_General_Config.process(this.instance, message);
    }
}