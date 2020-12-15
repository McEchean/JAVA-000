package io.kimmking.rpcfx.client.service.nettyclient;

import io.kimmking.rpcfx.client.service.nettyclient.util.NamedThreadFactory;
import io.kimmking.rpcfx.client.service.nettyclient.util.NettyEventLoopFactory;
import io.kimmking.rpcfx.client.service.nettyclient.vo.HostAndPortConfig;
import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpRequest;
import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpRequestContext;
import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NettyClient implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);
    /**
     * netty client bootstrap
     */
    private static final EventLoopGroup NIO_EVENT_LOOP_GROUP = NettyEventLoopFactory.eventLoopGroup(2, "NettyClientWorker");

    /**
     * netty client bootstrap
     */
    private static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP =  new DefaultEventLoop(null, new NamedThreadFactory("NettyResponsePromiseNotify"));



    private HostAndPortConfig config;

    /**
     * 当前使用的channel
     */
    Channel channel;

    /**
     * 重连的future
     */
    ScheduledFuture<?> scheduledFuture;


    public boolean isbIsConnectionOk() {
        return bIsConnectionOk;
    }

    boolean bIsConnectionOk;


    public NettyClient(HostAndPortConfig config) {
        this.config = config;

    }

    public void initConnection() {
        log.info("initConnection starts...");

        Bootstrap bootstrap;
        bootstrap = createBootstrap(config);
        ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
        log.info("current thread:{}", Thread.currentThread().getName());
        boolean ret = future.awaitUninterruptibly(2000, MILLISECONDS);

        boolean bIsSuccess = ret && future.isSuccess();
        if (!bIsSuccess) {
            bIsConnectionOk = false;
            log.error("host config:{}",config);
            throw new RuntimeException("连接失败");
        }

        cleanOldChannelAndCancelReconnect(future, channel);

        bIsConnectionOk = true;
    }

    private void cleanOldChannelAndCancelReconnect(ChannelFuture future, Channel oldChannel) {
        /**
         * 连接成功，关闭旧的channel，再用新的channel赋值给field
         */
        try {
            if (oldChannel != null) {
                try {
                    log.info("Close old netty channel " + oldChannel);
                    oldChannel.close();
                } catch (Exception e) {
                    log.error("e:{}", e);
                }
            }
        } finally {
            /**
             * 新channel覆盖field
             */
            NettyClient.this.channel = future.channel();
            NettyClient.this.bIsConnectionOk = true;
            log.info("connection is ok,new channel:{}", NettyClient.this.channel);
            if (NettyClient.this.scheduledFuture != null) {
                log.info("cancel scheduledFuture");
                NettyClient.this.scheduledFuture.cancel(true);
            }
        }
    }


    private Bootstrap createBootstrap(HostAndPortConfig config) {
        Bootstrap bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(NIO_EVENT_LOOP_GROUP);

        bootstrap.handler(new CustomChannelInitializer(bootstrap, config, this));
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        return bootstrap;
    }

    public static final AttributeKey<NettyHttpRequestContext> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    public NettyHttpResponse doPost(String url, Object body) {
        NettyHttpRequest request = new NettyHttpRequest(url, body);
        return doHttpRequest(request);
    }

    public NettyHttpResponse doGet(String url) {
        NettyHttpRequest request = new NettyHttpRequest(url, null);
        request.setHttpMethod(HttpMethod.GET);
        return doHttpRequest(request);
    }

    private NettyHttpResponse doHttpRequest(NettyHttpRequest request) {
//        /**
//         * 设置到channel中
//         */
//        DefaultEventLoop defaultEventLoop = new DefaultEventLoop(null, new NamedThreadFactory("elec-foot-ring"));
//
        Promise<NettyHttpResponse> defaultPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newPromise();
        NettyHttpRequestContext context = new NettyHttpRequestContext(request, defaultPromise);
        channel.attr(CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(context);

        ChannelFuture channelFuture = channel.writeAndFlush(request);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                System.out.println(Thread.currentThread().getName() + " 请求发送完成");
            }
        });

        return get(defaultPromise);
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


    public void reconnect() {
        log.info("start to reconnect,current thread:{}", Thread.currentThread().getName());
        Bootstrap bootstrap = createBootstrap(config);
        ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                ChannelFuture channelFuture = (ChannelFuture) future;
                if (future.isSuccess()) {
                    /**
                     * 清理旧的channel和取消重连future
                     */
                    NettyClient.this.cleanOldChannelAndCancelReconnect(channelFuture,
                            NettyClient.this.channel);
                    return;
                }

                scheduledFuture = channelFuture.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        reconnect();
                    }
                }, 2, TimeUnit.SECONDS);
                log.info("schedule a task to reconnect");
            }
        });
    }


    public static AttributeKey<Boolean> ACTIVE_CLOSE = AttributeKey.valueOf("ACTIVE_CLOSE");

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.attr(ACTIVE_CLOSE).set(true);
            channel.close().syncUninterruptibly();
            bIsConnectionOk = false;
        }

        if (scheduledFuture != null) {
            log.info("netty client is going to close,cancel scheduledFuture");
            scheduledFuture.cancel(true);
        }
    }

}
