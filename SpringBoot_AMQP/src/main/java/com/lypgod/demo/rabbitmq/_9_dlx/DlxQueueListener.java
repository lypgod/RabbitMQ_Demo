package com.lypgod.demo.rabbitmq._9_dlx;

import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

import static com.lypgod.demo.rabbitmq.util.R09_Dlx_Config.QUEUE_NAME_DLX_BUSINESS;

/**
 * @author lypgod
 */
@RabbitListener(queues = QUEUE_NAME_DLX_BUSINESS)
@Log4j2
public class DlxQueueListener {
    @RabbitHandler
    public void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.warn("----- [Listener] Received Message: '{}' ", message);
        channel.basicNack(tag, false, false);
    }
}