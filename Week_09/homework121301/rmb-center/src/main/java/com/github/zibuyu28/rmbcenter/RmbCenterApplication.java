package com.github.zibuyu28.rmbcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.github.zibuyu28.rmbcenter.mapper")
public class RmbCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmbCenterApplication.class, args);
	}

}
