package com.github.zibuyu28.mq;

import com.google.gson.Gson;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component("jmsResponseSender")
public class JMSResponseSender {

    private final JmsTemplate jmsTemplate;

    public JMSResponseSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(final MessageResponse resp) {
        Gson gson = new Gson();
        jmsTemplate.send("response.queue", session -> session.createObjectMessage(gson.toJson(resp)));
    }
}
