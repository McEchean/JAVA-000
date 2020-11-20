package com.github.zibuyu28.inbound;

import com.github.zibuyu28.filter.AddRequestHeadFilter;
import com.github.zibuyu28.filter.HttpRequestFilter;
import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.outbound.OutboundFactory;
import com.github.zibuyu28.outbound.OutboundHandlerType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final static Logger log = LoggerFactory.getLogger(HttpInboundHandler.class);

    @Autowired
    private final List<HttpRequestFilter> filters = new ArrayList<>();
    @Autowired
    private OutboundFactory outboundFactory;

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
            outboundFactory.getOutboundHandler().handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
