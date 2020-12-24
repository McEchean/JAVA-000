package io.kimmking.rpcfx.client.invoke;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.codec.Codec;
import io.kimmking.rpcfx.common.RpcfxException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Objects;

public class OkHttpInvoker implements Invoker {

    private Codec codec;

    private String host;

    private Integer port;

    public OkHttpInvoker(String host, Integer port, Codec codec) {
        this.codec = codec;
        this.host = host;
        this.port = port;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RpcfxResponse invoke(RpcfxRequest req) {
        String encodeReq = (String)codec.encode(req);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format("http://%s:%d/", this.host, this.port))
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), encodeReq))
                .build();
        try {
            String respJson = Objects.requireNonNull(client.newCall(request).execute().body()).string();
            return (RpcfxResponse)codec.decode(respJson);
        } catch (Exception e) {
            throw new RpcfxException("request to server error",e);
        }
    }
}
