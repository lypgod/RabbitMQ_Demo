package com.lypgod.demo.rabbitmq.util;

import com.lypgod.demo.rabbitmq._7_confirm.ConfirmListener;
import com.lypgod.demo.rabbitmq._7_confirm.ConfirmSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
@Profile("confirm")
public class R07_Confirm_Config implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
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
    public ConfirmListener receiver() {
        return new ConfirmListener();
    }

    @Profile("sender")
    @Bean
    public ConfirmSender sender() {
        return new ConfirmSender();
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //指定 ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        //指定 ReturnCallback
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.warn("----- ConfirmCallback Start -----");
        log.warn("消息唯一标识: {}", correlationData);
        log.warn("确认结果   : {}", ack);
        log.warn("失败原因   : {}", cause);
        log.warn("----- ConfirmCallback  End  -----");
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.warn("----- ReturnCallback Start -----");
        log.warn("message   : {}", message);
        log.warn("replyCode : {}", replyCode);
        log.warn("replyText : {}", replyText);
        log.warn("exchange  : {}", exchange);
        log.warn("routingKey: {}", routingKey);
        log.warn("----- ReturnCallback  End  -----");
    }
}
