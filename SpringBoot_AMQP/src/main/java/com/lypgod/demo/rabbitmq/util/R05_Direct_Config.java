package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._5_direct.DirectListener;
import com.lypgod.demo.rabbitmq._5_direct.DirectSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
@Profile("direct")
public class R05_Direct_Config {
    public static final String EXCHANGE_NAME_DIRECT = "exchange_direct";
    public static final String ROUTING_KEY_INFO = "info";
    public static final String ROUTING_KEY_WARN = "warn";
    public static final String ROUTING_KEY_ERROR = "error";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME_DIRECT);
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Binding binding1a(DirectExchange directExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(directExchange)
                .with(ROUTING_KEY_INFO);
    }

    @Profile("listener")
    @Bean
    public Binding binding1b(DirectExchange directExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(directExchange)
                .with(ROUTING_KEY_WARN);
    }

    @Profile("listener")
    @Bean
    public Binding binding1c(DirectExchange directExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(directExchange)
                .with(ROUTING_KEY_ERROR);
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Binding binding2a(DirectExchange directExchange, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2)
                .to(directExchange)
                .with(ROUTING_KEY_ERROR);
    }

    @Profile("listener")
    @Bean
    public DirectListener receiver() {
        return new DirectListener();
    }

    @Profile("sender")
    @Bean
    public DirectSender sender() {
        return new DirectSender();
    }
}
