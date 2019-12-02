package com.lypgod.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.*;

public class T05_DirectExchangeTest {
    private static Connection connection;
    private static Channel channel;


    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        channel.basicQos(1);

        RabbitMqUtils.declareExchange(channel, EXCHANGE_NAME_DIRECT, BuiltinExchangeType.DIRECT);

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_DIRECT_1);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DIRECT_1, EXCHANGE_NAME_DIRECT, ROUTING_KEY_ERROR);

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_DIRECT_2);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DIRECT_2, EXCHANGE_NAME_DIRECT, ROUTING_KEY_INFO);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DIRECT_2, EXCHANGE_NAME_DIRECT, ROUTING_KEY_WARN);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DIRECT_2, EXCHANGE_NAME_DIRECT, ROUTING_KEY_ERROR);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void directConsumer1() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_DIRECT_1, false);
    }

    @Test
    public void directConsumer2() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_DIRECT_2, false);
    }

    @Test
    public void directConsumer3() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_DIRECT_1, false);
    }

    @Test
    public void directConsumer4() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_DIRECT_2, false);
    }

    @Test
    public void produceMessageTest() throws IOException {
        for (int i = 0; i < 10; i++) {
            int random = new Random().nextInt(3);
            switch (random) {
                case 0:
                    RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_DIRECT, ROUTING_KEY_WARN, "Worker Message " + i);
                    break;
                case 1:
                    RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_DIRECT, ROUTING_KEY_INFO, "Worker Message " + i);
                    break;
                case 2:
                    RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_DIRECT, ROUTING_KEY_ERROR, "Worker Message " + i);
                    break;
                default:
                    break;

            }
        }
    }
}
