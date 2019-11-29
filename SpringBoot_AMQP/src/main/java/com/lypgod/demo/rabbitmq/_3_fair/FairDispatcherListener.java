package com.lypgod.demo.rabbitmq._3_fair;

import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lypgod.demo.rabbitmq.util.RabbitMqConfig.QUEUE_NAME_FAIR;
import static com.lypgod.demo.rabbitmq.util.RabbitMqConfig.TIME_FORMATTER;

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
        log.warn("----- [Listener {}] Received '{}' at {} -----", instance, message, TIME_FORMATTER.format(LocalDateTime.now()));

        int time = new Random().nextInt(2) + 1;
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.basicAck(tag, false);
        log.warn("----- [Listener {}] Processed '{}' with {} seconds and finished at {} -----", instance, message, time, TIME_FORMATTER.format(LocalDateTime.now()));
    }
}