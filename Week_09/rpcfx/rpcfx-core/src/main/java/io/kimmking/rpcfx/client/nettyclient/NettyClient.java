package io.kimmking.rpcfx.client.nettyclient;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.*;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.netty.util.CharsetUtil.UTF_8;

public class NettyClient {
    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    public static final ConcurrentHashMap<Channel, String> m = new ConcurrentHashMap<>();

    private static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP =  new DefaultEventLoop(null, new NamedThreadFactory("NettyResponsePromiseNotify"));

    public static final AttributeKey<Promise<String>> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    private final ChannelPoolMap<String, FixedChannelPool> poolMap = new AbstractChannelPoolMap<String, FixedChannelPool>() {
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

    public String handle(String url, RpcfxRequest req) throws Exception {
        String endpoint = getEndpoint(url);
        if(StringUtil.isNullOrEmpty(endpoint)) {
            log.error("error request url : {}", url);
            throw new RuntimeException("error request url");
        }
        FixedChannelPool fixedChannelPool = poolMap.get(endpoint);

        String reqJson = JSON.toJSONString(req);
        URI uri = new URI(url);
        ByteBuf byteBuf = Unpooled.copiedBuffer(reqJson, UTF_8);
        final FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), byteBuf);
        ;
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, byteBuf.readableBytes());
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE,HttpHeaders.Values.APPLICATION_JSON);

        Future<Channel> acquire = fixedChannelPool.acquire();
        Channel channel = acquire.get();
        Promise<String> objectPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newPromise();
        channel.attr(CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(objectPromise);
        channel.writeAndFlush(request).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isDone()) {
                    fixedChannelPool.release(channel);
                }
            }
        });
        return get(objectPromise);
    }

    public <V> V get(Promise<V> future) {
        if (!future.isDone()) {
            CountDownLatch l = new CountDownLatch(1);
            future.addListener(new GenericFutureListener<Future<? super V>>() {
                @Override
                public void operationComplete(Future<? super V> future) throws Exception {
                    log.info("received response,listener is invoked");
                    if (future.isDone()) {
                        // io线程会回调该listener
                        l.countDown();
                    }
                }
            });

            boolean interrupted = false;
            if (!future.isDone()) {
                try {
                    l.await(4, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("e:{}", e);
                    interrupted = true;
                }

            }

            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }

        if (future.isSuccess()) {
            return future.getNow();
        }
        log.error("wait result time out ");
        return null;
    }

    private String getEndpoint(String url) {
        url = url.startsWith("https://") ? url.substring(8) : url;
        url = url.startsWith("http://") ? url.substring(7) : url;
        String[] split = url.split("/");
        return split.length < 1 ? "": split[0];
    }
}
