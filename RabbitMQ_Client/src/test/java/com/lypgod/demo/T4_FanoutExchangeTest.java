package com.lypgod.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.*;

public class T4_FanoutExchangeTest {
    private static Connection connection;
    private static Channel channel;


    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();
        channel.basicQos(0);

        RabbitMqUtils.declareExchange(channel, EXCHANGE_NAME_FANOUT, BuiltinExchangeType.FANOUT);

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_FANOUT_1);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_FANOUT_1, EXCHANGE_NAME_FANOUT, "");

        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_FANOUT_2);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_FANOUT_2, EXCHANGE_NAME_FANOUT, "");
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void fanoutListener1() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_FANOUT_1, true);
    }

    @Test
    public void fanoutListener2() throws IOException, InterruptedException {
        RabbitMqUtils.listeningQueue(channel, QUEUE_NAME_FANOUT_2, true);
    }

    @Test
    public void sendFanoutMessage() throws IOException {
        for (int i = 0; i < 10; i++) {
            RabbitMqUtils.sendMessageToExchange(channel, EXCHANGE_NAME_FANOUT, "","Fanout Message " + i);
        }
    }
}
