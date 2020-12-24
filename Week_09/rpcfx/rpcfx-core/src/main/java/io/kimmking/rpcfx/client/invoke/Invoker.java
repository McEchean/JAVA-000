package io.kimmking.rpcfx.client.invoke;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

public interface Invoker {
    public RpcfxResponse invoke(RpcfxRequest request);
}
