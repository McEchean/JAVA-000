package com.github.zibuyu28;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class RedisLock {
    private static final Integer defaultExpireSecond = 30;
    private final JedisPool jedisPool;
    private final ThreadLocal<String> lockUnique = new ThreadLocal<>();
    private final InheritableThreadLocal<LeaseIns> leaseSignal = new InheritableThreadLocal<>();

    private static class LeaseIns {
        private Boolean finish;

        public LeaseIns(Boolean b) {
            this.finish = b;
        }

        public Boolean getFinish() {
            return finish;
        }

        public void setFinish(Boolean finish) {
            this.finish = finish;
        }
    }


    public RedisLock(String host, Integer port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public boolean Lock(Integer expireSecond, Boolean leasem) {
        SetParams setParams = new SetParams();
        setParams.nx().ex(expireSecond);
        UUID uuid = UUID.randomUUID();
        try (Jedis jedis = this.jedisPool.getResource()) {
            String set = jedis.set(LockKey.GLOBAL_LOCK.getKey(), uuid.toString(), setParams);
            if (set != null) {
                lockUnique.set(uuid.toString());
                if(leasem) {
                    leaseSignal.set(new LeaseIns(false));
                    leaseFunc();
                }
                return true;
            }
            return false;
        }
    }


    public boolean Lock() {
        return Lock(defaultExpireSecond, false);
    }

    public boolean unLock() {
        ClassLoader classLoader = RedisLock.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("del.lua");
        assert is != null;
        String dellLuaScript = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        try(Jedis jedis = this.jedisPool.getResource()) {
            return jedis.eval(dellLuaScript, 1, LockKey.GLOBAL_LOCK.getKey(), lockUnique.get()) != null;
        } finally {
            lockUnique.remove();
            System.out.println("设置为false");
            final LeaseIns leaseIns = leaseSignal.get();
            leaseIns.setFinish(true);
            leaseSignal.set(leaseIns);
            try {
                Thread.sleep(1000);
                leaseSignal.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void leaseFunc() {
        final String value = lockUnique.get();
        Thread t = new Thread(()-> {
            ClassLoader classLoader = RedisLock.class.getClassLoader();
            InputStream is = classLoader.getResourceAsStream("expire.lua");
            assert is != null;
            String expireLuaScript = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            while (!leaseSignal.get().getFinish()) {
                try(Jedis jedis = this.jedisPool.getResource()) {
                    System.out.println(Thread.currentThread().getName() + " 续期");
                    jedis.eval(expireLuaScript, 1, LockKey.GLOBAL_LOCK.getKey(), value, "10");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            leaseSignal.remove();
        });
        t.start();
    }

    public static void main(String[] args) {
        final RedisLock redisLock = new RedisLock("127.0.0.1", 6379);
        final CountDownLatch c = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 开始抢锁");
                        Thread.sleep(10);
                        while (!redisLock.Lock(defaultExpireSecond, true)) {
                            Thread.sleep(1);
                        }
                        System.out.println(Thread.currentThread().getName() + " get lock");
                        Thread.sleep(1000);
                        c.countDown();
                        redisLock.unLock();
                        System.out.println(Thread.currentThread().getName() + " unlock");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

    }
}
