package io.kimmking.rpcfx.client.proxy;

import io.kimmking.rpcfx.client.Client;
import io.kimmking.rpcfx.client.codec.JSONCodec;
import io.kimmking.rpcfx.client.invoke.OkHttpInvoker;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

public class JDKProxy implements Client {
    private final ConcurrentHashMap<Class<?>, Object> classObjectConcurrentHashMap = new ConcurrentHashMap<>();
    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> t, String host, Integer port) {
        if(classObjectConcurrentHashMap.containsKey(t)) {
            return (T)classObjectConcurrentHashMap.get(t);
        }
        JDKInvokeHandler jdkInvokeHandler = new JDKInvokeHandler(new OkHttpInvoker(host, port, new JSONCodec()));
        T instance = (T) Proxy.newProxyInstance(JDKProxy.class.getClassLoader(), new Class[]{t}, jdkInvokeHandler);
        classObjectConcurrentHashMap.put(t, instance);
        return instance;
    }
}
