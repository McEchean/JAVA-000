package com.github.zibuyu28.demo;

import com.github.zibuyu28.KmqBroker;
import com.github.zibuyu28.KmqConsumer;
import com.github.zibuyu28.KmqMessage;
import com.github.zibuyu28.KmqProducer;

import java.io.IOException;

public class KmqDemo {

    public static void main(String[] args) throws InterruptedException, IOException {

        String topic = "kk.test";
        KmqBroker<Order> broker = new KmqBroker<>();
        broker.createTopic(topic);

        KmqConsumer<Order> consumer = broker.createConsumer();
        consumer.subscribe(topic);
        final boolean[] flag = new boolean[1];
        flag[0] = true;
        new Thread(() -> {
            while (flag[0]) {
                KmqMessage<Order> message = consumer.poll();
                if(null != message) {
                    System.out.println("reveive message : " + message.toString());
//                    System.out.println(message.getBody());
                }
            }
            System.out.println("程序退出。");
        }).start();

        KmqProducer<Order> producer = broker.createProducer();
        for (int i = 0; i < 1000; i++) {
            Order order = new Order(1000L + i, System.currentTimeMillis(), "USD2CNY", 6.51d);
            producer.send(topic, new KmqMessage<>(null, order));
        }
        Thread.sleep(500);
        System.out.println("点击任何键，发送一条消息；点击q或e，退出程序。");
        while (true) {
            char c = (char) System.in.read();
            if(c > 20) {
                System.out.println(c);
                producer.send(topic, new KmqMessage<>(null, new Order(100000L + c, System.currentTimeMillis(), "USD2CNY", 6.52d)));
            }

            if( c == 'q' || c == 'e') break;
        }

        flag[0] = false;

    }
}
