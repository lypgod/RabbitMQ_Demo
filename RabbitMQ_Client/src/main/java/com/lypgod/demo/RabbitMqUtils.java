package com.lypgod.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class RabbitMqUtils {
    static final String QUEUE_NAME_SIMPLE = "simple_queue";
    static final String QUEUE_NAME_WORK = "work_queue";

    static final String EXCHANGE_NAME_FANOUT = "exchange_fanout";
    static final String QUEUE_NAME_FANOUT_1 = "fanout_queue_1";
    static final String QUEUE_NAME_FANOUT_2 = "fanout_queue_2";

    static final String EXCHANGE_NAME_DIRECT = "exchange_direct";
    static final String QUEUE_NAME_DIRECT_1 = "direct_queue_1";
    static final String QUEUE_NAME_DIRECT_2 = "direct_queue_2";
    static final String ROUTING_KEY_INFO = "info";
    static final String ROUTING_KEY_WARN = "warn";
    static final String ROUTING_KEY_ERROR = "error";

    static final String EXCHANGE_NAME_TOPIC = "exchange_topic";
    static final String QUEUE_NAME_TOPIC_1 = "topic_queue_1";
    static final String QUEUE_NAME_TOPIC_2 = "topic_queue_2";
    static final String ROUTING_KEY_GOODS_ADD = "goods.add";
    static final String ROUTING_KEY_GOODS_DELETE = "goods.delete";
    static final String ROUTING_KEY_GOODS_ALL = "goods.#";

    static Connection getDefaultConnection() throws IOException, TimeoutException {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置RabbitMQ服务主机地址，默认localhost
        connectionFactory.setHost("192.168.2.10");
        // 设置RabbitMQ服务主机端口，默认5672
        connectionFactory.setPort(5672);
        // 设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/lypgod");
        // 设置连接用户名，默认guest
        connectionFactory.setUsername("admin");
        // 设置连接密码，默认guest
        connectionFactory.setPassword("admin");
        // 创建连接
        return connectionFactory.newConnection();
    }

    static void listeningQueue(Channel channel, String queueName, boolean autoAck) throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss SSS");
        channel.basicConsume(queueName, autoAck,
                (consumerTag, message) -> {
                    System.out.println("----- Thread(" + Thread.currentThread().getName() + ") received message at " + formatter.format(LocalDateTime.now()) + " -----");
                    System.out.println("ConsumerTag: " + consumerTag);
                    System.out.println("DeliveryTag: " + message.getEnvelope().getDeliveryTag());
                    System.out.println("交换机名称: " + message.getEnvelope().getExchange());
                    System.out.println("路由键: " + message.getEnvelope().getRoutingKey());
                    System.out.println("收到消息: " + new String(message.getBody(), StandardCharsets.UTF_8));

                    int time = new Random().nextInt(2) + 1;
                    try {
                        TimeUnit.SECONDS.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!autoAck) {
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    }
                    System.out.println("----- Process spent " + time + " seconds and finished at " + formatter.format(LocalDateTime.now()) + " -----");
                },
                consumerTag -> {

                });

        TimeUnit.HOURS.sleep(1);
    }

    static void declareQueue(Channel channel, String queueName) throws IOException {
        /*
         * 声明队列
         *  参数1：队列名称
         *  参数2：消息是否持久化
         *  参数3：当前消息队列是否属于当前连接对象独占
         *  参数4：消息消费完毕之后是否自动删除
         *  参数5：附加参数
         */
        channel.queueDeclare(queueName, false, false, false, null);
    }

    static void declareExchange(Channel channel, String exchangeName, BuiltinExchangeType exchangeType) throws IOException {
        channel.exchangeDeclare(exchangeName, exchangeType);
    }

    static void bindQueueToExchange(Channel channel, String queueName, String exchangeName, String routingKey) throws IOException {
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    static void sendMessage(Channel channel, String routingKey, String message) throws IOException {
        /*
         * 发送消息
         *  参数1：消息要发送的交换机，不写默认Default Exchange
         *  参数2：当前消息路由键。简单消息模式，路由键可以直接写成队列地址
         *  参数3：附加数据
         *  参数4：消息体
         */
        channel.basicPublish("", routingKey, null, message.getBytes());
        System.out.println("Message sent to " + routingKey + ": " + message);
    }

    static void sendMessageToExchange(Channel channel, String exchangeName, String routingKey, String message) throws IOException {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        System.out.println("Message sent to [" + exchangeName + "], with routing key [" + routingKey + "]: " + message);
    }
}