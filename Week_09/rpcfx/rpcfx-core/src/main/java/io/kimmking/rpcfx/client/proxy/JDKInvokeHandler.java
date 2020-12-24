package io.kimmking.rpcfx.client.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.invoke.Invoker;
import io.kimmking.rpcfx.common.RpcfxException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JDKInvokeHandler implements InvocationHandler {
    private Invoker invoker;

    public JDKInvokeHandler(Invoker invoker) {
        this.invoker = invoker;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(proxy.getClass().getInterfaces()[0].getName());
        request.setMethod(method.getName());
        request.setParams(args);
        try {
            RpcfxResponse response = invoker.invoke(request);
            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException
            if(!response.getStatus()) {
                throw new RpcfxException("invoke failed", response.getException());
            }
            return JSON.parse(response.getResult().toString());
        } catch (Exception e){
            throw new RpcfxException("invoke rpc failed", e);
        }
    }
}
