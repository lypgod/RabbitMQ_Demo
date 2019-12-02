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

public class T06_TopicExchangeTest {
    private static Connection connection;
    private static Channel channel;


    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        channel.basicQos(1);

        RabbitMqUtils.declareExchange(channel, EXCHANGE_NAME_TOPIC, BuiltinExchangeType.TOPIC);

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_TOPIC_1);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_TOPIC_1, EXCHANGE_NAME_TOPIC, ROUTING_KEY_GOODS_ADD);

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_TOPIC_2);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_TOPIC_2, EXCHANGE_NAME_TOPIC, ROUTING_KEY_GOODS_ALL);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void topicConsumer1() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_TOPIC_1, false);
    }

    @Test
    public void topicConsumer2() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_TOPIC_2, false);
    }

    @Test
    public void topicConsumer3() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_TOPIC_1, false);
    }

    @Test
    public void topicConsumer4() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_TOPIC_2, false);
    }

    @Test
    public void produceTopicMessage() throws IOException {
        for (int i = 0; i < 10; i++) {
            int random = new Random().nextInt(2);
            switch (random) {
                case 0:
                    RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_TOPIC, ROUTING_KEY_GOODS_ADD, "Topic Message " + i);
                    break;
                case 1:
                    RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_TOPIC, ROUTING_KEY_GOODS_DELETE, "Topic Message " + i);
                    break;
                default:
                    break;

            }
        }
    }
}
