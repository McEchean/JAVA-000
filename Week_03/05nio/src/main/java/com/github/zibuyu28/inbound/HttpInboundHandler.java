package com.github.zibuyu28.inbound;

import com.github.zibuyu28.filter.AddRequestHeadFilter;
import com.github.zibuyu28.filter.HttpRequestFilter;
import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.outbound.OutboundFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final static Logger log = LoggerFactory.getLogger(HttpInboundHandler.class);

    private final List<HttpRequestFilter> filters;
    private final HttpOutboundHandler handler;


    public HttpInboundHandler() {
        this.filters = new ArrayList<>();
        this.filters.add(new AddRequestHeadFilter());
        this.handler = OutboundFactory.newOutboundHandler();
    }


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
            for (HttpRequestFilter ft :filters) {
                ft.filter(fullRequest, ctx);
            }
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
