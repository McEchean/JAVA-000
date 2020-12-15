package io.kimmking.rpcfx.client.service.nettyclient;

import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpRequestContext;
import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

/**
 * http请求响应的处理器
 */
@Slf4j
public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse fullHttpResponse) throws Exception {
        String s = fullHttpResponse.content().toString(CharsetUtil.UTF_8);

        NettyHttpResponse nettyHttpResponse = NettyHttpResponse.successResponse(s);
        NettyHttpRequestContext nettyHttpRequestContext = (NettyHttpRequestContext) ctx.channel().attr(NettyClient.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).get();

        log.info("req url:{},params:{},resp:{}",
                nettyHttpRequestContext.getNettyHttpRequest().getFullUrl(),
                nettyHttpRequestContext.getNettyHttpRequest().getBody(),
                nettyHttpResponse);
        Promise<NettyHttpResponse> promise = nettyHttpRequestContext.getDefaultPromise();
        promise.setSuccess(nettyHttpResponse);
    }


}
