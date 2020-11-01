package com.github.zibuyu28;

import com.github.zibuyu28.inbound.HttpInboundServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerApplication {
    private static final Logger log = LoggerFactory.getLogger(NettyServerApplication.class);
    public static void main(String[] args) throws Exception {
        HttpInboundServer server = new HttpInboundServer();
        try {
            server.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("run server error : {}", e.getMessage());
        }
    }
}
