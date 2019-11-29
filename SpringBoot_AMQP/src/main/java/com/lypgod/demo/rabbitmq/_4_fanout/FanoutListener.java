package com.lypgod.demo.rabbitmq._4_fanout;

import com.lypgod.demo.rabbitmq.util.RabbitMqConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author lypgod
 */
@Log4j2
public class FanoutListener {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String message) {
        RabbitMqConfig.process(1, message);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String message) {
        RabbitMqConfig.process(2, message);
    }
}