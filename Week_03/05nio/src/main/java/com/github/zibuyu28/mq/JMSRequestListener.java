package com.github.zibuyu28.mq;

import com.github.zibuyu28.outbound.httpclient4.HttpClient4OutboundHandler;
import com.google.gson.Gson;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Service("jmsRequestListener")
public class JMSRequestListener implements MessageListener {

    @Autowired
    private HttpClient4OutboundHandler httpClient4OutboundHandler;

    @Override
    public void onMessage(Message message) {
        ObjectMessage m = (ObjectMessage) message;
        try {
            String strObj = (String) m.getObject();
            Gson gson = new Gson();
            MessageRequest object = gson.fromJson(strObj, MessageRequest.class);
            httpClient4OutboundHandler.handle2(object);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
