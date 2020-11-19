package com.github.zibuyu28.outbound;

import com.github.zibuyu28.outbound.httpclient4.HttpClient4OutboundHandler;
import com.github.zibuyu28.outbound.nettyclient.NettyClientOutboundHandler;
import com.github.zibuyu28.outbound.okhttp.OkHttpClientOutboundHandler;
import com.github.zibuyu28.util.Prop;
import org.springframework.stereotype.Component;

@Component
public class OutboundFactory {
    private HttpClient4OutboundHandler httpClient4OutboundHandler;
    private NettyClientOutboundHandler nettyClientOutboundHandler;
    private OkHttpClientOutboundHandler okHttpClientOutboundHandler;

    public OutboundFactory(HttpClient4OutboundHandler httpClient4OutboundHandler, NettyClientOutboundHandler nettyClientOutboundHandler, OkHttpClientOutboundHandler okHttpClientOutboundHandler) {
        this.httpClient4OutboundHandler = httpClient4OutboundHandler;
        this.nettyClientOutboundHandler = nettyClientOutboundHandler;
        this.okHttpClientOutboundHandler = okHttpClientOutboundHandler;
    }

    public HttpOutboundHandler getOutboundHandler() {
        OutboundHandlerType outboundHandlerType = OutboundHandlerType.parseType(Prop.getOrDefault("outbound.type", OutboundHandlerType.HttpClient4.getName()));
        switch (outboundHandlerType) {
            case HttpClient4:
                return this.httpClient4OutboundHandler;
            case NettyClient:
                return this.nettyClientOutboundHandler;
            case OkHttpClient:
                return this.okHttpClientOutboundHandler;
        }
        return null;
    }
}
