package com.lypgod.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.lypgod.demo.RabbitMqUtils.*;

public class T10_DeadLetterExchangeTest {
    private static Connection connection;
    private static Channel channel;

    @BeforeClass
    public static void beforeClass() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getDefaultConnection();
        channel = connection.createChannel();

        // 声明死信队列
        RabbitMqUtils.declareExchange(channel, EXCHANGE_NAME_DLX, BuiltinExchangeType.TOPIC);
        RabbitMqUtils.declareQueue(channel, QUEUE_NAME_DLX);
        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DLX, EXCHANGE_NAME_DLX, "#");

        // 声明业务交换机和队列
        RabbitMqUtils.declareExchange(channel, EXCHANGE_NAME_TOPIC, BuiltinExchangeType.TOPIC);

        // 指定死信发送的Exchange
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", EXCHANGE_NAME_DLX);
        // arguments要设置到声明队列上
        channel.queueDeclare(QUEUE_NAME_DIRECT_1, true, false, false, arguments);

        RabbitMqUtils.bindQueueToExchange(channel, QUEUE_NAME_DIRECT_1, EXCHANGE_NAME_TOPIC, ROUTING_KEY_DLX);
    }

    @AfterClass
    public static void afterClass() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Test
    public void dlxQueueListener() throws IOException, InterruptedException {
        RabbitMqUtils.listeningDlxQueue(channel, QUEUE_NAME_DIRECT_1, true);
    }

    @Test
    public void sendDlxMessage() throws IOException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .build();
        RabbitMqUtils.sendMessage(channel, EXCHANGE_NAME_TOPIC, "dlx.save", properties,"DLX Message");
    }
}
