package com.github.zibuyu28.outbound.nettyclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class NettyClientOutboundInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        ChannelPipeline pipeline = nioSocketChannel.pipeline();

        pipeline.addLast(new HttpResponseDecoder());
        pipeline.addLast(new HttpRequestEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new NettyClientHandler());
    }
}
