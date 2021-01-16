package com.github.zibuyu28.homework010901.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
    private void consumer(ConsumerRecord<Integer, String> record) {
        log.info("topic : {}, get message : {}, ", record.topic(), record.value());
    }
}
