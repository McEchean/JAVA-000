package com.github.zibuyu28.homework010901.controller;

import com.github.zibuyu28.homework010901.entity.Message;
import com.github.zibuyu28.homework010901.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;


@RestController
public class TestController {

    @Autowired
    private Producer producer;

    @PostMapping("/message")
    private ResponseEntity<String> testKafka(@RequestBody Message message) {
        message.setTime(Date.valueOf(LocalDate.now()));
        message.setId(UUID.randomUUID().toString());
        producer.sendMessageSync("test-topic",message);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
