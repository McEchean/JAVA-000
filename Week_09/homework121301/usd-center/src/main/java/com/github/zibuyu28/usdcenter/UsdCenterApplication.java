package com.github.zibuyu28.usdcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.github.zibuyu28.usdcenter.mapper")
public class UsdCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsdCenterApplication.class, args);
	}

}
