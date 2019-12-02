package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._3_fair.FairDispatcherListener;
import com.lypgod.demo.rabbitmq._3_fair.FairDispatcherSender;
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
@Profile("fair")
public class R03_Fair_Config {
    public static final String QUEUE_NAME_FAIR = "fair_queue";

    @Bean
    public Queue fairDispatcherQueue() {
        return QueueBuilder.nonDurable(QUEUE_NAME_FAIR).build();
    }

    @Profile("listener")
    @Bean
    public FairDispatcherListener listener1() {
        return new FairDispatcherListener(1);
    }

    @Profile("listener")
    @Bean
    public FairDispatcherListener listener2() {
        return new FairDispatcherListener(2);
    }

    @Profile("sender")
    @Bean
    public FairDispatcherSender sender() {
        return new FairDispatcherSender();
    }
}
