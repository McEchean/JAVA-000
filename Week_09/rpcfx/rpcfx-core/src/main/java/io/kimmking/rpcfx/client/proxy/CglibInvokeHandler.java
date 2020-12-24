package io.kimmking.rpcfx.client.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.invoke.Invoker;
import io.kimmking.rpcfx.common.RpcfxException;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibInvokeHandler implements MethodInterceptor {

    private final Invoker invoker;

    public CglibInvokeHandler(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(o.getClass().getInterfaces()[0].getName());
        request.setMethod(method.getName());
        request.setParams(objects);
        try {
            RpcfxResponse response = invoker.invoke(request);
            if (!response.getStatus()) {
                throw new RpcfxException("invoke failed", response.getException());
            }
            return JSON.parse(response.getResult().toString());
        } catch (Exception e) {
            throw new RpcfxException("invoke rpc failed", e);
        }
    }
}
