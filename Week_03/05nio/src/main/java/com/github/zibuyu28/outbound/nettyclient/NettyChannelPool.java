package com.github.zibuyu28.outbound.nettyclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyChannelPool implements ChannelPoolHandler {
    private static final Logger log = LoggerFactory.getLogger(NettyChannelPool.class);

    @Override
    public void channelReleased(Channel ch) throws Exception {
        ch.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpContentDecompressor());
        pipeline.addLast(new HttpObjectAggregator(123433));
        pipeline.addLast(new NettyClientHandler());
    }
}
