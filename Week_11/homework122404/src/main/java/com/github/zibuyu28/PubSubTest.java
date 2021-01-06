package com.github.zibuyu28;

import redis.clients.jedis.JedisPool;


public class PubSubTest {

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        final String channel = "mychannel";
        Subscriber subscriber = new Subscriber(jedisPool, channel);
        subscriber.start();
        Publisher publisher = new Publisher(jedisPool);
        try {
            Thread.sleep(1000);
            for (int i = 0; i < 10; i++) {
                publisher.pub(channel, String.format("pub message : %d", i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finish");
    }
}
