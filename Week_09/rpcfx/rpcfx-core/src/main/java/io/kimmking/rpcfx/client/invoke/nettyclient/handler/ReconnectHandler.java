package io.kimmking.rpcfx.client.invoke.nettyclient.handler;

import io.kimmking.rpcfx.client.invoke.nettyclient.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReconnectHandler extends ChannelInboundHandlerAdapter {


    private NettyClient tcpClient;



    public ReconnectHandler(NettyClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                tcpClient.heartBeat();
//            }
//        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Attribute<Boolean> attr = ctx.channel().attr(NettyClient.ACTIVE_CLOSE);
        Boolean activeClose = attr.get();
        if (activeClose != null && activeClose) {
            log.info("active close,no reconnect");
            ctx.fireChannelInactive();
        } else {
            ctx.fireChannelInactive();
            tcpClient.reconnect();
        }
    }


}