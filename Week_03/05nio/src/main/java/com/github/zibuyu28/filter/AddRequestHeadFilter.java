package com.github.zibuyu28.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddRequestHeadFilter implements HttpRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AddRequestHeadFilter.class);
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().add("nio","hello filter");
    }
}
