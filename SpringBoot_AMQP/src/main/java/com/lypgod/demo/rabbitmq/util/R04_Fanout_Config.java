package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._4_fanout.FanoutListener;
import com.lypgod.demo.rabbitmq._4_fanout.FanoutSender;
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
@Profile("fanout")
public class R04_Fanout_Config {
    public static final String EXCHANGE_NAME_FANOUT = "exchange_fanout";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME_FANOUT);
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanoutExchange);
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Binding binding2(FanoutExchange fanoutExchange, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanoutExchange);
    }

    @Profile("listener")
    @Bean
    public FanoutListener receiver() {
        return new FanoutListener();
    }

    @Profile("sender")
    @Bean
    public FanoutSender sender() {
        return new FanoutSender();
    }
}
