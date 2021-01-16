package com.github.zibuyu28.homework010901.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zibuyu28.homework010901.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2SmileEncoder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class Producer {
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageAsync(String topic, Message message) {
        log.info("kafka client start to send message");
        Jackson2SmileEncoder jackson2SmileEncoder = new Jackson2SmileEncoder();
        ObjectMapper mapper = new ObjectMapper();
        try {
            final String s = mapper.writeValueAsString(message);
            kafkaTemplate.send(topic, s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("json encode exception", e);
        }
    }

    public void sendMessageSync(String topic, Message message) {
        log.info("kafka client start to send message");
        ObjectMapper mapper = new ObjectMapper();
        try {
            final String s = mapper.writeValueAsString(message);
            final ListenableFuture<SendResult<Integer, String>> send = kafkaTemplate.send(topic, s);
            final SendResult<Integer, String> integerStringSendResult = send.get();
            log.info("send message result : {}", integerStringSendResult.toString());
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error("json encode exception", e);
            throw new RuntimeException(e);
        }
    }
}
