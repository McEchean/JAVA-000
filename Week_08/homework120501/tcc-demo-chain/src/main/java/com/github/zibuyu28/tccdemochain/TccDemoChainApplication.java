package com.github.zibuyu28.tccdemochain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan(value = "com.github.zibuyu28.tccdemochain.mapper")
@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TccDemoChainApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccDemoChainApplication.class, args);
	}

}
