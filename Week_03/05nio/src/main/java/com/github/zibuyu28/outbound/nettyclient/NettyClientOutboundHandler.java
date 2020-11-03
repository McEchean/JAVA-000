package com.github.zibuyu28.outbound.nettyclient;

import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.router.HttpEndpointRouter;
import com.github.zibuyu28.router.RouterFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class NettyClientOutboundHandler implements HttpOutboundHandler {
    private static final Logger log = LoggerFactory.getLogger(NettyClientOutboundHandler.class);

    public static class F {
        public static NettyClientOutboundHandler INSTANCE = new NettyClientOutboundHandler();
    }


    private final EventLoopGroup clientEventLoop;
    private final Bootstrap clientBootstrap;
    private final HttpEndpointRouter router;

    private NettyClientOutboundHandler() {
        this.router = RouterFactory.newRouter();
        this.clientEventLoop = new NioEventLoopGroup();
        this.clientBootstrap = new Bootstrap().option(ChannelOption.SO_KEEPALIVE, true);

        this.clientBootstrap.group(this.clientEventLoop).channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new NettyClientOutboundInitializer());

    }

    public static NettyClientOutboundHandler getInstance() {
        return F.INSTANCE;
    }

    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws Exception {
        String endpoint = availableEndpoint();
        String[] split = endpoint.split(":");
        if (split.length != 2) {
            log.error("available endpoint not correct, endpoint : {}", endpoint);
            throw new IllegalArgumentException(String.format("available endpoint not correct, endpoint : %s", endpoint));
        }

        ChannelFuture future = clientBootstrap.connect(split[0], Integer.parseInt(split[1])).sync();

        URI uri = new URI(String.format("http://%s%s", endpoint, fullRequest.uri()));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(fullRequest.protocolVersion(), fullRequest.method(),
                uri.toASCIIString(), Unpooled.wrappedBuffer("".getBytes()));
        request.headers().setAll(fullRequest.headers());
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        // 发送http请求
        future.channel().write(request);
        future.channel().flush();

        // todo 这里一定要注释掉，原因目前不知道，还是不太理解netty的channel
        final Channel channel = future.channel();
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                Object res = channel.attr(AttributeKey.valueOf("res")).get();
                if (res instanceof FullHttpResponse ){
                    FullHttpResponse f = (FullHttpResponse)res;
                    if (!HttpUtil.isKeepAlive(fullRequest)) {
                        ctx.write(f).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        //response.headers().set(CONNECTION, KEEP_ALIVE);
                        ctx.write(f);
                    }
                    ctx.flush();
                    ctx.close();
                }
            }
        });
    }

    private String availableEndpoint() throws Exception {
        String addr = router.getEndPoint();
        if (addr.startsWith("http")) {
            addr = addr.replaceFirst("http://", "");
        }
        return addr;
    }
}
