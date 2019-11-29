package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._1_simple.SimpleQueueListener;
import com.lypgod.demo.rabbitmq._1_simple.SimpleQueueSender;
import com.lypgod.demo.rabbitmq._2_round_robin.RoundRobinListener;
import com.lypgod.demo.rabbitmq._2_round_robin.RoundRobinSender;
import com.lypgod.demo.rabbitmq._3_fair.FairDispatcherListener;
import com.lypgod.demo.rabbitmq._3_fair.FairDispatcherSender;
import com.lypgod.demo.rabbitmq._4_fanout.FanoutListener;
import com.lypgod.demo.rabbitmq._4_fanout.FanoutSender;
import com.lypgod.demo.rabbitmq._5_direct.DirectListener;
import com.lypgod.demo.rabbitmq._5_direct.DirectSender;
import com.lypgod.demo.rabbitmq._6_topic.TopicListener;
import com.lypgod.demo.rabbitmq._6_topic.TopicSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
public class RabbitMqConfig {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss SSS");
    public static final String QUEUE_NAME_SIMPLE = "simple_queue";
    public static final String QUEUE_NAME_ROUND = "round_queue";
    public static final String QUEUE_NAME_FAIR = "fair_queue";

    public static void process(int instance, String message) {
        log.warn("----- [Listener {}] Received '{}' at {} -----", instance, message, TIME_FORMATTER.format(LocalDateTime.now()));

        int time = new Random().nextInt(2) + 1;
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.warn("----- [Listener {}] Processed '{}' with {} seconds and finished at {} -----", instance, message, time, TIME_FORMATTER.format(LocalDateTime.now()));
    }

    public static final String EXCHANGE_NAME_FANOUT = "exchange_fanout";

    public static final String EXCHANGE_NAME_DIRECT = "exchange_direct";
    public static final String ROUTING_KEY_INFO = "info";
    public static final String ROUTING_KEY_WARN = "warn";
    public static final String ROUTING_KEY_ERROR = "error";

    public static final String EXCHANGE_NAME_TOPIC = "exchange_topic";
    public static final String ROUTING_KEY_TOPIC_ORANGE = "*.orange.*";
    public static final String ROUTING_KEY_TOPIC_RABBIT = "*.*.rabbit";
    public static final String ROUTING_KEY_TOPIC_LAZY = "lazy.#";

    /**
     * Simple Queue
     */
    @Profile("simple")
    private static class SimpleQueueConfig {
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

    /**
     * Round Robin
     */
    @Profile("round")
    private static class RoundRobinConfig {
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

    /**
     * Fair Dispatch
     */
    @Profile("fair")
    private static class FairDispatcherConfig {
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

    /**
     * Fanout Exchange
     */
    @Profile("fanout")
    private static class FanoutConfig {
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

    /**
     * Direct Exchange
     */
    @Profile("direct")
    private static class DirectConfig {
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

    /**
     * Topic Exchange
     */
    @Profile("topic")
    private static class TopicConfig {
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
}
