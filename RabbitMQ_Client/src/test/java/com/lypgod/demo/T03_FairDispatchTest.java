package com.lypgod.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.QUEUE_NAME_WORK;

public class T03_FairDispatchTest {
    private static Connection connection;
    private static Channel channel;


    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        channel.basicQos(1);
        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_WORK);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void fairDispatchListener1() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_WORK, false);
    }

    @Test
    public void fairDispatchListener2() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_WORK, false);
    }

    @Test
    public void sendFairDispatchMessage() throws IOException {
        for (int i = 0; i < 10; i++) {
            RabbitMqUtils.sendMessage(channel, QUEUE_NAME_WORK,"Work Message " + i);
        }
    }
}
