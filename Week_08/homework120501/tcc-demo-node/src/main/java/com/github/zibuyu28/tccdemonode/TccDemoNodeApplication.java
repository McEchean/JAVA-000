package com.github.zibuyu28.tccdemonode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
@MapperScan(value = {"com.github.zibuyu28.tccdemonode.mapper"})
public class TccDemoNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccDemoNodeApplication.class, args);
	}

}
