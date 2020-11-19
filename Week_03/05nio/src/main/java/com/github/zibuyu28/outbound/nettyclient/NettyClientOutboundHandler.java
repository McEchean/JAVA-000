package com.github.zibuyu28.outbound.nettyclient;

import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.router.HttpEndpointRouter;
import com.github.zibuyu28.router.RouterFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Lazy
public class NettyClientOutboundHandler implements HttpOutboundHandler {
    private static final Logger log = LoggerFactory.getLogger(NettyClientOutboundHandler.class);

//    private static class F {
//        private static final NettyClientOutboundHandler INSTANCE = new NettyClientOutboundHandler();
//    }

    public static final AttributeKey<ChannelHandlerContext> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    private final ChannelPoolMap<String, FixedChannelPool> poolMap2 = new AbstractChannelPoolMap<String, FixedChannelPool>() {
        @Override
        protected FixedChannelPool newPool(String endpoint) {
            String[] split = endpoint.split(":");
            if (split.length != 2) {
                log.error("available endpoint not correct, endpoint : {}", endpoint);
                throw new IllegalArgumentException(String.format("available endpoint not correct, endpoint : %s", endpoint));
            }
            NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap().option(ChannelOption.TCP_NODELAY, true);
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .remoteAddress(split[0], Integer.parseInt(split[1]));

            return new FixedChannelPool(bootstrap, new NettyChannelPool(), 10000);
        }
    };

//    private final HttpEndpointRouter router;

    @Autowired
    private RouterFactory routerFactory;

//    private NettyClientOutboundHandler() {
//        this.router = RouterFactory.newRouter();
//    }

//    public static NettyClientOutboundHandler getInstance() {
//        return F.INSTANCE;
//    }

    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctxn) throws Exception {
        String endpoint = availableEndpoint();
        FixedChannelPool fixedChannelPool = poolMap2.get(endpoint);

        URI uri = new URI(String.format("http://%s%s", endpoint, fullRequest.uri()));
        final DefaultFullHttpRequest request = new DefaultFullHttpRequest(fullRequest.protocolVersion(), fullRequest.method(),
                uri.toASCIIString(), Unpooled.wrappedBuffer("".getBytes()));
        request.headers().setAll(fullRequest.headers());
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

        Future<Channel> acquire = fixedChannelPool.acquire();
        Channel channel = acquire.get();
        channel.attr(CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(ctxn);
        channel.writeAndFlush(request).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isDone()) {
                    fixedChannelPool.release(channel);
                }
            }
        });

        // todo 改动的过程
//        acquire.addListener(new GenericFutureListener<Future<? super Channel>>() {
//            @Override
//            public void operationComplete(Future<? super Channel> future) throws Exception {
//
//                Object res = channel.attr(AttributeKey.valueOf("res")).get();
////                System.out.println(res);
//                if (res instanceof FullHttpResponse) {
//                    FullHttpResponse f = (FullHttpResponse) res;
//                    if (!HttpUtil.isKeepAlive(fullRequest)) {
//                        ctxn.write(f).addListener(ChannelFutureListener.CLOSE);
//                    } else {
//                        //response.headers().set(CONNECTION, KEEP_ALIVE);
//                        ctxn.write(f);
//                    }
//                }
//                ctxn.flush();
//                fixedChannelPool.release(channel);
//                ctxn.close();
//
//            }
//        });

//        ChannelFuture future = clientBootstrap.connect(split[0], Integer.parseInt(split[1])).sync();
//
//
//        // 发送http请求
//        future.channel().write(request);
//        future.channel().flush();
//
//        final Channel channel = future.channel();
//        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
//            @Override
//            public void operationComplete(Future<? super Void> future) throws Exception {
//                Object res = channel.attr(AttributeKey.valueOf("res")).get();
//                if (res instanceof FullHttpResponse ){
//                    FullHttpResponse f = (FullHttpResponse)res;
//                    if (!HttpUtil.isKeepAlive(fullRequest)) {
//                        ctx.write(f).addListener(ChannelFutureListener.CLOSE);
//                    } else {
//                        //response.headers().set(CONNECTION, KEEP_ALIVE);
//                        ctx.write(f);
//                    }
//                    ctx.flush();
//                    ctx.close();
//                }
//            }
//        });
    }

    private String availableEndpoint() throws Exception {
        String addr = routerFactory.getRouter().getEndPoint();
        if (addr.startsWith("http")) {
            addr = addr.replaceFirst("http://", "");
        }
        return addr;
    }
}
