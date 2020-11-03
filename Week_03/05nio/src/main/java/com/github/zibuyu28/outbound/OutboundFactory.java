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
                return HttpClient4OutboundHandler.getInstance();
            case NettyClient:
                return NettyClientOutboundHandler.getInstance();
            case OkHttpClient:
                return OkHttpClientOutboundHandler.getInstance();
        }
        return null;
    }
}
