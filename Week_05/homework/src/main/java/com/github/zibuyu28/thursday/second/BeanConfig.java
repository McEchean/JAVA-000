package com.github.zibuyu28.thursday.second;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public Student getStudent() {
        Student student = new Student();
        student.setName("笼子里");
        return student;
    }
}
