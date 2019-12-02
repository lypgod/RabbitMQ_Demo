package com.lypgod.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.QUEUE_NAME_WORK;

public class T02_RoundRobinTest {
    private static Connection connection;
    private static Channel channel;


    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_WORK);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void roundRobinListener1() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_WORK, true);
    }

    @Test
    public void roundRobinListener2() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_WORK, true);
    }

    @Test
    public void sendRoundRobinMessage() throws IOException {
        for (int i = 0; i < 10; i++) {
            RabbitMqUtils.sendMessage(channel, QUEUE_NAME_WORK,"Work Message " + i);
        }
    }
}
