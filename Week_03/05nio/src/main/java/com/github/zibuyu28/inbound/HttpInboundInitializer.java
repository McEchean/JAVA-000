package com.github.zibuyu28.inbound;

import com.github.zibuyu28.outbound.nettyclient.NettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired // 获取多例bean
    private ObjectFactory<HttpInboundHandler> beans;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();

        p.addLast(new HttpServerCodec());

        p.addLast(new HttpObjectAggregator(1024 * 1024));
        HttpInboundHandler object = beans.getObject();
        log.info("获取的多例 : {}", object);
        p.addLast(object);
    }
}
