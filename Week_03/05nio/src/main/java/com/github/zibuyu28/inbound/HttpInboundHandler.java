package com.github.zibuyu28.inbound;

import com.github.zibuyu28.filter.HttpRequestFilter;
import com.github.zibuyu28.mq.JMSRequestSender;
import com.github.zibuyu28.mq.JMSResponseListener;
import com.github.zibuyu28.mq.MessageRequest;
import com.github.zibuyu28.outbound.OutboundFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Scope("prototype")
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final static Logger log = LoggerFactory.getLogger(HttpInboundHandler.class);

    @Autowired
    private final List<HttpRequestFilter> filters = new ArrayList<>();
    @Autowired
    private OutboundFactory outboundFactory;
    @Autowired
    private JMSResponseListener listener;
    @Autowired
    private JMSRequestSender sender;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("channelRead 接收到请求, 获取到 msg 类型是 : {}", msg.getClass().getName());
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
//            for (HttpRequestFilter ft :filters) {
//                ft.filter(fullRequest, ctx);
//            }
            listener.Register(ctx);
            MessageRequest messageRequest = new MessageRequest();
            HashMap<String,String> headers = new HashMap<>();
            messageRequest.setCtxAddr(ctx.toString());
            messageRequest.setReqURI(fullRequest.uri());
            fullRequest.headers().forEach(item->headers.put(item.getKey(), item.getValue()));
            messageRequest.setHeaders(headers);
            sender.send(messageRequest);
//            outboundFactory.getOutboundHandler().handle(fullRequest, ctx);

            // send msg;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
