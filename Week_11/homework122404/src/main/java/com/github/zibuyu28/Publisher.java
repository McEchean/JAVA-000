package com.github.zibuyu28;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Publisher {
    private final JedisPool jedisPool;

    public Publisher(String host, Integer port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void pub(String channel,String message) {
        try (Jedis jedis = jedisPool.getResource()){
            final Long publish = jedis.publish(channel, message);
            System.out.println("成功推送消息: " + message);
        }
    }
}
