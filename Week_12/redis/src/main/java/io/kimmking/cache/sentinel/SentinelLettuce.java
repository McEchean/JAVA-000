package io.kimmking.cache.sentinel;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;


public class SentinelLettuce {

    private static final RedisClient redisClient = create();

    private static RedisClient create() {
        return RedisClient.create("redis-sentinel://localhost:26379,localhost:26380/0#mymaster");
    }

    public static StatefulRedisConnection<String, String> getConnect() {
        return redisClient.connect();
    }

    public static void close() {
        redisClient.shutdown();
    }
}
