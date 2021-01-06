package com.github.zibuyu28;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    private final JedisPool jedisPool;
    private final String targetChannel;

    public Subscriber(String host, Integer port, String channel) {
        this.jedisPool = new JedisPool(host, port);
        this.targetChannel = channel;
    }

    public Subscriber(JedisPool jedisPool, String channel) {
        this.jedisPool = jedisPool;
        this.targetChannel = channel;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals(this.targetChannel)) {
            System.out.println("通道 " + this.targetChannel + " 收到消息: " + message);
        }
    }

    public void start() {
        Thread t = new Thread(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(this, this.targetChannel);
            }
        });
        t.start();
    }

}
