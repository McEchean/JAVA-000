package com.github.zibuyu28.homework112802;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class Homework112802Application {

    public static void main(String[] args) {
        SpringApplication.run(Homework112802Application.class, args);
    }

}
