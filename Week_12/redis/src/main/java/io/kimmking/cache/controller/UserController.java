package io.kimmking.cache.controller;

import io.kimmking.cache.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/user/find")
    User find(Integer id) {
        return new User(1, "KK", 28);
    }

    @RequestMapping("/user/list")
    List<User> list() {
        return Arrays.asList(new User(1, "KK", 28),
                new User(2, "CC", 18));
    }

    @GetMapping("/test")
    public void test() {
        redisTemplate.opsForValue().set("redistemplate", String.valueOf(System.currentTimeMillis()));
        final String redistemplate = redisTemplate.opsForValue().get("redistemplate");
        System.out.println(redistemplate);
    }
}