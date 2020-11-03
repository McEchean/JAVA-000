package com.github.zibuyu28.outbound.nettyclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.AttributeKey;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DefaultFullHttpResponse resp = null;
        if (msg instanceof HttpResponse && msg instanceof HttpContent) {
            HttpResponse response = (HttpResponse) msg;
            ByteBuf buf = null;
            HttpContent content = (HttpContent)msg;
            buf = content.content();
            resp = new DefaultFullHttpResponse(response.protocolVersion(), response.status(),buf);
            resp.headers().setAll(response.headers());
        } else {
            throw new RuntimeException("response from server not http response");
        }

        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(resp.protocolVersion(), resp.status(), Unpooled.wrappedBuffer(resp.content()));
            response.headers().setInt("Content-Length", Integer.parseInt(resp.headers().get("Content-Length")));

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            ctx.channel().attr(AttributeKey.valueOf("res")).set(response);
            ctx.flush();
            ctx.close();
        }
    }

}
