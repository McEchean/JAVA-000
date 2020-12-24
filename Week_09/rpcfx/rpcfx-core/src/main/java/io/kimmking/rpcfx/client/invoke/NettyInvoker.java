package io.kimmking.rpcfx.client.invoke;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.codec.Codec;
import io.kimmking.rpcfx.client.invoke.nettyclient.NettyClient;
import io.kimmking.rpcfx.client.invoke.nettyclient.vo.HostAndPortConfig;
import io.kimmking.rpcfx.client.invoke.nettyclient.vo.NettyHttpResponse;
import io.kimmking.rpcfx.common.RpcfxException;

import java.io.IOException;

public class NettyInvoker implements Invoker {
    private Codec codec;

    private String host;

    private Integer port;

    public NettyInvoker(String host, Integer port, Codec codec) {
        this.codec = codec;
        this.host = host;
        this.port = port;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RpcfxResponse invoke(RpcfxRequest req) {
        NettyClient nettyClient = new NettyClient(new HostAndPortConfig(this.host, this.port));
        nettyClient.initConnection();
        try {
            String encodeReq = (String) codec.encode(req);
            NettyHttpResponse nettyHttpResponse = nettyClient.doPost(String.format("http://%s:%d/", this.host, this.port), encodeReq);
            return (RpcfxResponse) codec.decode(nettyHttpResponse.getBody());
        } catch (Exception e) {
            throw new RpcfxException("netty request", e);
        } finally {
            try {
                nettyClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
