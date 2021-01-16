package com.github.zibuyu28;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

public class ActiveMQTopic {
    private final Connection connection;

    public ActiveMQTopic() {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendMessage() {
        try {
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic("test.topic");

            final MessageProducer producer = session.createProducer(destination);

            String message = "Hello world! From : " + Thread.currentThread().getName() + ", time : " + System.currentTimeMillis();

            final TextMessage textMessage = session.createTextMessage(message);

            producer.send(textMessage);

            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic("test.topic");

            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListenerIns());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static class MessageListenerIns implements MessageListener {
        public void onMessage(Message message) {
            try {
                System.out.println("Message listener receive message : " + ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ActiveMQTopic activeMQTopic = new ActiveMQTopic();
        final long start = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            while (start + 5000 > System.currentTimeMillis()) {
                activeMQTopic.sendMessage();
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            while (start + 5000 > System.currentTimeMillis()) {
                activeMQTopic.sendMessage();
            }
            countDownLatch.countDown();
        }).start();

        activeMQTopic.receiveMessage();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finish");
    }
}
