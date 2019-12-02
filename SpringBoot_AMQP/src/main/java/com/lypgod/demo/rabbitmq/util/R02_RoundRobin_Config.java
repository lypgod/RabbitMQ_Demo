package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._2_round_robin.RoundRobinListener;
import com.lypgod.demo.rabbitmq._2_round_robin.RoundRobinSender;
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
@Profile("round")
public class R02_RoundRobin_Config {
    public static final String QUEUE_NAME_ROUND = "round_queue";

    @Bean
    public Queue roundRobinQueue() {
        return QueueBuilder.durable(QUEUE_NAME_ROUND).build();
    }

    @Profile("listener")
    @Bean
    public RoundRobinListener listener1() {
        return new RoundRobinListener(1);
    }

    @Profile("listener")
    @Bean
    public RoundRobinListener listener2() {
        return new RoundRobinListener(2);
    }

    @Profile("sender")
    @Bean
    public RoundRobinSender sender() {
        return new RoundRobinSender();
    }
}
