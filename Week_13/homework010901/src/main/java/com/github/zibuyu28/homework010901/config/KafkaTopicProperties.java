package com.github.zibuyu28.homework010901.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("kafka.topic")
public class KafkaTopicProperties {
    private String groupId;

    private String[] topicName;
}
