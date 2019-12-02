package com.lypgod.demo.rabbitmq._4_fanout;

import com.lypgod.demo.rabbitmq.util.R00_General_Config;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author lypgod
 */
@Log4j2
public class FanoutListener {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String message) {
        R00_General_Config.process(1, message);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String message) {
        R00_General_Config.process(2, message);
    }
}