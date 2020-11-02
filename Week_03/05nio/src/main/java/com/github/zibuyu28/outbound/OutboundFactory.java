package com.github.zibuyu28.outbound;

import com.github.zibuyu28.outbound.httpclient4.HttpClient4OutboundHandler;
import com.github.zibuyu28.outbound.nettyclient.NettyClientOutboundHandler;
import com.github.zibuyu28.outbound.okhttp.OkHttpClientOutboundHandler;
import com.github.zibuyu28.util.Prop;

public class OutboundFactory {
    public static HttpOutboundHandler newOutboundHandler() {
        OutboundHandlerType outboundHandlerType = OutboundHandlerType.parseType(Prop.getOrDefault("outbound.type", OutboundHandlerType.HttpClient4.getName()));
        switch (outboundHandlerType) {
            case HttpClient4:
                return new HttpClient4OutboundHandler();
            case NettyClient:
                return new NettyClientOutboundHandler();
            case OkHttpClient:
                return new OkHttpClientOutboundHandler();
        }
        return null;
    }
}
