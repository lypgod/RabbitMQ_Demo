package com.lypgod.demo.rabbitmq.util;

import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lypgod
 */
@Configuration
@Log4j2
public class R00_General_Config {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss SSS");

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

    public static void processWithAck(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, int instance) throws IOException {
        log.warn("----- [Listener {}] Received '{}' at {} -----", instance, message, TIME_FORMATTER.format(LocalDateTime.now()));

        int time = new Random().nextInt(2) + 1;
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.basicAck(tag, false);
//        channel.basicNack(tag,false,true);
//        channel.basicReject(tag,false);

        log.warn("----- [Listener {}] Processed '{}' with {} seconds and finished at {} -----", instance, message, time, TIME_FORMATTER.format(LocalDateTime.now()));
    }
}
