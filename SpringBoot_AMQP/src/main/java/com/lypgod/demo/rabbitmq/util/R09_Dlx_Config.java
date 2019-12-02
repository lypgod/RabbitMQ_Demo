package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._9_dlx.DlxQueueListener;
import com.lypgod.demo.rabbitmq._9_dlx.DlxQueueSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
@Profile("dlx")
public class R09_Dlx_Config {
    public static final String QUEUE_NAME_DLX_BUSINESS = "business_queue";
    public static final String EXCHANGE_NAME_DLX_BUSINESS = "business_exchange";
    public static final String ROUTING_KEY_DLX_BUSINESS = "business_routing_key";

    /**
     * 死信队列：
     */
    public static final String QUEUE_NAME_DLX = "dlx_queue";
    public static final String ROUTING_KEY_DLX = "dlx_routing_key";
    public static final String EXCHANGE_NAME_DLX = "dlx_exchange";
    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    @Bean
    public Queue businessQueue() {
        //将普通队列绑定到死信交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(DEAD_LETTER_QUEUE_KEY, EXCHANGE_NAME_DLX);
        args.put(DEAD_LETTER_ROUTING_KEY, ROUTING_KEY_DLX);
        return QueueBuilder.durable(QUEUE_NAME_DLX_BUSINESS).withArguments(args).build();
    }

    @Bean
    public DirectExchange businessExchange() {
        return new DirectExchange(EXCHANGE_NAME_DLX_BUSINESS);
    }

    @Bean
    public Binding businessBinding(DirectExchange businessExchange, Queue businessQueue) {
        return BindingBuilder.bind(businessQueue)
                .to(businessExchange)
                .with(ROUTING_KEY_DLX_BUSINESS);
    }

    /**
     * 死信队列：
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(QUEUE_NAME_DLX).build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(EXCHANGE_NAME_DLX);
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(ROUTING_KEY_DLX);
    }

    @Profile("listener")
    @Bean
    public DlxQueueListener listener() {
        return new DlxQueueListener();
    }

    @Profile("sender")
    @Bean
    public DlxQueueSender sender() {
        return new DlxQueueSender();
    }
}
