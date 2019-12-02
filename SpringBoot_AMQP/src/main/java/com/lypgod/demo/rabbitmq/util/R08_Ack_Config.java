package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._1_simple.SimpleQueueSender;
import com.lypgod.demo.rabbitmq._8_ack.AckQueueListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
@Profile("ack")
public class R08_Ack_Config {
    public static final String QUEUE_NAME_ACK = "ack_queue";

    @Bean
    public Queue ackQueue() {
        return QueueBuilder.durable(QUEUE_NAME_ACK).build();
    }

    @Profile("listener")
    @Bean
    public AckQueueListener listener() {
        return new AckQueueListener();
    }

    @Profile("sender")
    @Bean
    public SimpleQueueSender sender() {
        return new SimpleQueueSender();
    }
}
