package com.github.zibuyu28;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GlobalCounter {

    private final JedisPool jedisPool;
    private static final String productPrefix = "global::product::";

    public GlobalCounter(String host, Integer port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public boolean decrease(Integer productID, Integer count) {
        String key = String.format("%s%d", productPrefix, productID);
        ClassLoader classLoader = GlobalCounter.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("decrease.lua");
        final String decreaseLuaScript = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        try (Jedis jedis = jedisPool.getResource()) {
            final Object eval = jedis.eval(decreaseLuaScript, 1, key, String.valueOf(count));
            return (eval != null && (long) eval >= 0);
        }
    }

    public static void main(String[] args) {
        GlobalCounter globalCounter = new GlobalCounter("127.0.0.1", 6379);
        final CountDownLatch c = new CountDownLatch(101);
        AtomicInteger success = new AtomicInteger();
        for (int i = 0; i < 101; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean res = globalCounter.decrease(1, 1);
                    System.out.println(Thread.currentThread().getName() + " " + res);
                    c.countDown();
                    if(res)success.addAndGet(1);
                }
            });
            t.setName("工作线程-" + i);
            t.start();
        }

        try {
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finish");
        System.out.println("sale count : " + success.get());
    }
}
