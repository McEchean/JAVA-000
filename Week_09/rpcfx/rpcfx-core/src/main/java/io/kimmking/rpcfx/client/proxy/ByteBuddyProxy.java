package io.kimmking.rpcfx.client.proxy;

import io.kimmking.rpcfx.client.ARpcfx;
import io.kimmking.rpcfx.client.Client;
import io.kimmking.rpcfx.client.codec.JSONCodec;
import io.kimmking.rpcfx.client.invoke.NettyInvoker;
import io.kimmking.rpcfx.client.invoke.OkHttpInvoker;
import io.kimmking.rpcfx.common.RpcfxException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.concurrent.ConcurrentHashMap;

public class ByteBuddyProxy implements Client {
    private final ConcurrentHashMap<Class<?>, Object> classObjectConcurrentHashMap = new ConcurrentHashMap<>();
    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> t, String host, Integer port) {
        if(classObjectConcurrentHashMap.containsKey(t)) {
            return (T)classObjectConcurrentHashMap.get(t);
        }
        try {
            T instance = (T) new ByteBuddy().subclass(Object.class)
                    .implement(t)
                    .method(ElementMatchers.isAnnotatedWith(ARpcfx.class))
                    .intercept(InvocationHandlerAdapter.of(new ByteBuddyHandler(new NettyInvoker(host, port, new JSONCodec()))))
                    .make()
                    .load(ByteBuddyProxy.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
            classObjectConcurrentHashMap.put(t, instance);
            return instance;
        } catch (Exception e) {
            throw new RpcfxException("byte buddy new instance", e);
        }
    }
}
