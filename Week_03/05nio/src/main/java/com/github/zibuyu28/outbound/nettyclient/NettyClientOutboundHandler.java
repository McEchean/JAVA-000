package com.github.zibuyu28.outbound.nettyclient;

import com.github.zibuyu28.inbound.HttpInboundInitializer;
import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.router.HttpEndpointRouter;
import com.github.zibuyu28.router.RandomEndpointRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientOutboundHandler implements HttpOutboundHandler {
    private static final Logger log = LoggerFactory.getLogger(NettyClientOutboundHandler.class);

    public static final ConcurrentHashMap<String, ChannelHandlerContext> ccid = new ConcurrentHashMap<>();

    private final EventLoopGroup clientEventLoop;
    private final Bootstrap clientBootstrap;
    private final HttpEndpointRouter router;

    public NettyClientOutboundHandler() {
        this.router = new RandomEndpointRouter();
        this.clientEventLoop = new NioEventLoopGroup();
        this.clientBootstrap = new Bootstrap().option(ChannelOption.SO_KEEPALIVE, true);

        this.clientBootstrap.group(this.clientEventLoop).channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new NettyClientOutboundInitializer());

    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws Exception {
        String endpoint = availableEndpoint();
        String[] split = endpoint.split(":");
        if(split.length != 2) {
            log.error("available endpoint not correct, endpoint : {}", endpoint);
            throw new IllegalArgumentException(String.format("available endpoint not correct, endpoint : %s", endpoint));
        }

        ChannelFuture future = clientBootstrap.connect(split[0], Integer.parseInt(split[1])).sync();

        URI uri = new URI(String.format("http://%s%s",endpoint, fullRequest.uri()));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(fullRequest.protocolVersion(), fullRequest.method(),
                uri.toASCIIString(), Unpooled.wrappedBuffer("".getBytes()));
        request.headers().setAll(fullRequest.headers());
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        // 发送http请求
        ccid.put(future.channel().id().asLongText(), ctx);
        future.channel().write(request);
        future.channel().flush();

        // todo 这里一定要注释掉，原因目前不知道，还是不太理解netty的channel
//        future.channel().closeFuture().sync();
    }

    private String availableEndpoint() throws Exception {
        String addr = router.getEndPoint();
        if(addr.startsWith("http")) {
            addr = addr.replaceFirst("http://", "");
        }
        return addr;
    }
}
