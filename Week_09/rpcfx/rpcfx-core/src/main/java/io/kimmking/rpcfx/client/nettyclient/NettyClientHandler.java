package io.kimmking.rpcfx.client.nettyclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.Promise;

import java.nio.charset.StandardCharsets;

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
        String res = resp.content().toString(StandardCharsets.UTF_8);
        System.out.println(res);

        Promise<String> stringPromise = ctx.channel().attr(NettyClient.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).get();
        stringPromise.setSuccess(res);
    }
}
