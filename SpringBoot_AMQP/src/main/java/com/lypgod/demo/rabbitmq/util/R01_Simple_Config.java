package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._1_simple.SimpleQueueListener;
import com.lypgod.demo.rabbitmq._1_simple.SimpleQueueSender;
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
@Profile("simple")
public class R01_Simple_Config {
    public static final String QUEUE_NAME_SIMPLE = "simple_queue";

    @Bean
    public Queue simpleQueue() {
        return QueueBuilder.durable(QUEUE_NAME_SIMPLE).build();
    }

    @Profile("listener")
    @Bean
    public SimpleQueueListener listener() {
        return new SimpleQueueListener();
    }

    @Profile("sender")
    @Bean
    public SimpleQueueSender sender() {
        return new SimpleQueueSender();
    }
}
