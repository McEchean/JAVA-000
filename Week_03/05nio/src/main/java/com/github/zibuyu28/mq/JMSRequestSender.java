package com.github.zibuyu28.mq;

import com.google.gson.Gson;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component("jmsRequestSender")
@Slf4j
public class JMSRequestSender {

    private final JmsTemplate jmsTemplate;

    public JMSRequestSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(final MessageRequest req) {
        Gson gson = new Gson();
        String s = gson.toJson(req);
        log.info("消息是：{}", s);
        jmsTemplate.send("request.queue", session -> session.createObjectMessage(s));
    }
}
