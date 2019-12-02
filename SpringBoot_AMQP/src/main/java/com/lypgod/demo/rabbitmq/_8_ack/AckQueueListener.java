package com.lypgod.demo.rabbitmq._8_ack;

import com.lypgod.demo.rabbitmq.util.R00_General_Config;
import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

import static com.lypgod.demo.rabbitmq.util.R08_Ack_Config.QUEUE_NAME_ACK;

/**
 * @author lypgod
 */
@RabbitListener(queues = QUEUE_NAME_ACK)
@Log4j2
public class AckQueueListener {
    @RabbitHandler
    public void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        R00_General_Config.processWithAck(message, channel, tag, 0);
    }
}