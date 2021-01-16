package com.github.zibuyu28.homework010901.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContextTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        final List<String> collect = Arrays.stream(applicationContext.getBeanDefinitionNames())
                .collect(Collectors.toList());

        collect.forEach(log::info);

        final Object kafkaTopicName = applicationContext.getBean("kafkaTopicName");
        log.info(kafkaTopicName.getClass().getName());
    }
}
