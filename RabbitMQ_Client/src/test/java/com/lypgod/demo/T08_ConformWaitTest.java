package com.lypgod.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.QUEUE_NAME_SIMPLE;

public class T08_ConformWaitTest {
    private static Connection connection;
    private static Channel channel;

    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_SIMPLE);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void simpleQueueListener() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_SIMPLE, true);
    }

    @Test
    public void sendSimpleMessage() throws IOException {
        RabbitMqUtils.sendMessageConfirmWait(channel, QUEUE_NAME_SIMPLE, "Simple Message");
    }
}
