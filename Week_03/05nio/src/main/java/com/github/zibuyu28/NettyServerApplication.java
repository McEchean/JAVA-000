package com.github.zibuyu28;

import com.github.zibuyu28.inbound.HttpInboundServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyServerApplication {
    private static final Logger log = LoggerFactory.getLogger(NettyServerApplication.class);
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        HttpInboundServer server = (HttpInboundServer)ctx.getBean("httpInboundServer");
        try {
            server.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("run server error : {}", e.getMessage());
        }
    }
}
