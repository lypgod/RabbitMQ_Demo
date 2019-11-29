package com.lypgod.demo.rabbitmq._6_topic;

import com.lypgod.demo.rabbitmq.util.RabbitMqConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author lypgod
 */
@Log4j2
public class TopicListener {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String message) {
        RabbitMqConfig.process(1, message);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String message) {
        RabbitMqConfig.process(2, message);
    }
}