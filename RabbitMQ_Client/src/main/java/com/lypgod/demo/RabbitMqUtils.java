package com.lypgod.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
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

    static final String EXCHANGE_NAME_DLX = "exchange_dlx";
    static final String QUEUE_NAME_DLX = "dlx_queue";
    static final String ROUTING_KEY_DLX = "dlx.#";

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

    static void listeningDlxQueue(Channel channel, String queueName, boolean autoAck) throws IOException, InterruptedException {
        channel.basicConsume(queueName, autoAck,
                (consumerTag, message) -> {
                    System.out.println("----- received message -----");
                    System.out.println("ConsumerTag: " + consumerTag);
                    System.out.println("DeliveryTag: " + message.getEnvelope().getDeliveryTag());
                    System.out.println("交换机名称: " + message.getEnvelope().getExchange());
                    System.out.println("路由键: " + message.getEnvelope().getRoutingKey());
                    System.out.println("properties: " + message.getProperties());
                    System.out.println("收到消息: " + new String(message.getBody(), StandardCharsets.UTF_8));
                    System.out.println("----------------------------");
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

    static void sendMessage(Channel channel, String exchangeName, String routingKey, AMQP.BasicProperties properties, String message) throws IOException {
        channel.basicPublish(exchangeName, routingKey, true, properties, message.getBytes());
        System.out.println("Message sent to [" + exchangeName + "], with routing key [" + routingKey + "]: " + message);
    }

    static void sendMessageTx(Channel channel, String routingKey, String message) {
        try {
            channel.txSelect();
            channel.basicPublish("", routingKey, null, message.getBytes());
            channel.txCommit();
            System.out.println("Message sent to " + routingKey + ": " + message);
        } catch (IOException e) {
            try {
                channel.txRollback();
                System.out.println("Message [" + message + "] rolled back.");
            } catch (IOException ex) {
                System.out.println("Message [" + message + "] rollback failed!");
                ex.printStackTrace();
            }
        }
    }

    static void sendMessageConfirmWait(Channel channel, String routingKey, String message) throws IOException {
        channel.confirmSelect();
        channel.basicPublish("", routingKey, null, message.getBytes());
        try {
            if (channel.waitForConfirms()) {
                System.out.println("Message sent to " + routingKey + ": " + message);
            } else {
                System.out.println("Message [" + message + "] confirmed failure!");
            }
        } catch (InterruptedException e) {
            System.out.println("Message [" + message + "] wait for confirm interrupted!");
            e.printStackTrace();
        }
    }

    static void sendMessageConfirmCallback(Channel channel, String routingKey, String message) throws IOException, InterruptedException {
        // channel设置为confirm模式
        channel.confirmSelect();
        // 记录未确认的消息标识
        final SortedSet<Long> unconfirmedSet = Collections.synchronizedSortedSet(new TreeSet<>());
        // 通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 处理返回确认成功
             * @param deliveryTag 如果是多条，这个就是最后一条消息的tag
             * @param multiple 是否多条
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                System.out.println("Broker返回消息发送成功确认, deliveryTag：" + deliveryTag + "， multiple：" + multiple);

                // 批量发送ack确认，客户端就把此次消息序号之前的记录全删除，代表这个消息之前的消息broker都已接收到
                if (multiple) {
                    unconfirmedSet.headSet(deliveryTag + 1).clear();
                } else {
                    // broker单个返回ack确认
                    unconfirmedSet.remove(deliveryTag);
                }
            }
            /**
             * 处理返回确认消息丢失
             * @param deliveryTag 如果是多条，这个就是最后一条消息的tag
             * @param multiple 是否多条
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                System.out.println("Broker返回消息丢失, deliveryTag：" + deliveryTag + "， multiple：" + multiple);
            }
        });

        for (int i = 0; i < 10; i++) {
            String msg = "Confirm callback message [" + i + "]";
            long tag = channel.getNextPublishSeqNo();
            //发送消息
            channel.basicPublish("", routingKey, null, msg.getBytes());

            System.out.println("Confirm callback message [" + i + "] has been sent with tag: " + tag);
            unconfirmedSet.add(tag);

            TimeUnit.SECONDS.sleep(1);
        }

        // 等待回调确认
        TimeUnit.SECONDS.sleep(2);
    }
}