package io.kimmking.rpcfx.client.proxy;

import io.kimmking.rpcfx.client.Client;
import io.kimmking.rpcfx.client.codec.JSONCodec;
import io.kimmking.rpcfx.client.invoke.OkHttpInvoker;
import org.springframework.cglib.proxy.Enhancer;

import java.util.concurrent.ConcurrentHashMap;

public class CglibProxy implements Client {

    private final ConcurrentHashMap<Class<?>, Object> classObjectConcurrentHashMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> serviceClass, final String host, final Integer port) {

        if(classObjectConcurrentHashMap.containsKey(serviceClass)) {
            return (T)classObjectConcurrentHashMap.get(serviceClass);
        }

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(serviceClass);

        enhancer.setCallback(new CglibInvokeHandler(new OkHttpInvoker(host, port, new JSONCodec())));

        T t = (T) enhancer.create();

        classObjectConcurrentHashMap.put(serviceClass, t);

        return t;
    }
}
