package io.kimmking.rpcfx.client.invoke.nettyclient;

import io.kimmking.rpcfx.client.invoke.nettyclient.handler.HttpJsonRequestEncoder;
import io.kimmking.rpcfx.client.invoke.nettyclient.handler.ReconnectHandler;
import io.kimmking.rpcfx.client.invoke.nettyclient.vo.HostAndPortConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;

public class CustomChannelInitializer extends ChannelInitializer<Channel> {

    private final HostAndPortConfig config;

    private final NettyClient nettyClient;

    public CustomChannelInitializer(Bootstrap bootstrap,
                                    HostAndPortConfig config, NettyClient nettyClient) {
        super();
        this.config = config;
        this.nettyClient = nettyClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // http客户端编解码器，包括了客户端http请求编码，http响应的解码
        pipeline.addLast(new HttpClientCodec());

        // 把多个HTTP请求中的数据组装成一个
        pipeline.addLast(new HttpObjectAggregator(65536));

        // 用于处理大数据流
        pipeline.addLast(new ChunkedWriteHandler());

        /**
         * 重连handler
         */
        pipeline.addLast(new ReconnectHandler(nettyClient));

        /**
         * 发送业务数据前，进行json编码
         */
        pipeline.addLast(new HttpJsonRequestEncoder());


        pipeline.addLast(new HttpResponseHandler());

    }


}