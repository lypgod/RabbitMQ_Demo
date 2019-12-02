package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._6_topic.TopicListener;
import com.lypgod.demo.rabbitmq._6_topic.TopicSender;
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
@Profile("topic")
public class R06_Topic_Config {
    public static final String EXCHANGE_NAME_TOPIC = "exchange_topic";
    public static final String ROUTING_KEY_TOPIC_ORANGE = "*.orange.*";
    public static final String ROUTING_KEY_TOPIC_RABBIT = "*.*.rabbit";
    public static final String ROUTING_KEY_TOPIC_LAZY = "lazy.#";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME_TOPIC);
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Profile("listener")
    @Bean
    public Binding binding1a(TopicExchange topicExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(topicExchange)
                .with(ROUTING_KEY_TOPIC_ORANGE);
    }

    @Profile("listener")
    @Bean
    public Binding binding1b(TopicExchange topicExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(topicExchange)
                .with(ROUTING_KEY_TOPIC_RABBIT);
    }

    @Profile("listener")
    @Bean
    public Binding binding2a(TopicExchange topicExchange, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2)
                .to(topicExchange)
                .with(ROUTING_KEY_TOPIC_LAZY);
    }

    @Profile("listener")
    @Bean
    public TopicListener receiver() {
        return new TopicListener();
    }

    @Profile("sender")
    @Bean
    public TopicSender sender() {
        return new TopicSender();
    }
}
