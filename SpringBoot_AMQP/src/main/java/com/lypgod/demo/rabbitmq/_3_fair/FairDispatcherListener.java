package com.lypgod.demo.rabbitmq._3_fair;

import com.lypgod.demo.rabbitmq.util.R00_General_Config;
import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

import static com.lypgod.demo.rabbitmq.util.R03_Fair_Config.QUEUE_NAME_FAIR;

/**
 * @author lypgod
 */
@Log4j2
public class FairDispatcherListener {
    private final int instance;

    public FairDispatcherListener(int i) {
        this.instance = i;
    }

    @RabbitListener(queues = QUEUE_NAME_FAIR, ackMode = "MANUAL")
    public void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        R00_General_Config.processWithAck(message, channel, tag, instance);
    }
}