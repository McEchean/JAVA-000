package com.github.zibuyu28.mq;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Service("jmsResponseListener")
@Slf4j
public class JMSResponseListener implements MessageListener {

    private ConcurrentHashMap<String, ChannelHandlerContext> ctxm = new ConcurrentHashMap<>();

    public void Register(ChannelHandlerContext ctx) {
        this.ctxm.put(ctx.toString(), ctx);
    }
    @Override
    public void onMessage(Message message) {
        ObjectMessage m = (ObjectMessage) message;
        try {
            String strObj = (String) m.getObject();
            Gson gson = new Gson();
            MessageResponse object = gson.fromJson(strObj, MessageResponse.class);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(object.getResponseBody()));
            response.headers().setInt("Content-Length", object.getResponseBody().length);

            ChannelHandlerContext channelHandlerContext = ctxm.get(object.getCtxAddr());
            if(channelHandlerContext == null) {
                log.warn("message {} not find ctx", object);
                return;
            }
            channelHandlerContext.write(response);
            channelHandlerContext.flush();
            ctxm.remove(object.getCtxAddr());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
