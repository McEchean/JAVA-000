package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.service.nettyclient.NettyClient;
import io.kimmking.rpcfx.client.service.nettyclient.vo.HostAndPortConfig;
import io.kimmking.rpcfx.client.service.nettyclient.vo.NettyHttpResponse;
import io.kimmking.rpcfx.common.RpcfxException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.DefaultMethodCall;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class RpcfxAOP {

    public static ConcurrentHashMap<String, Object> objectCache = new ConcurrentHashMap<>();

    public static  <T> T invoker(Class<T> klass, String url) {
        if(objectCache.containsKey(klass.getName())) {
            return (T)objectCache.get(klass.getName());
        }
        try {
            T o = (T)new ByteBuddy().subclass(Object.class)
                    .implement(klass)
                    .method(ElementMatchers.isAnnotatedWith(ARpcfx.class))
                    .intercept(DefaultMethodCall.prioritize(klass))
                    .make()
                    .load(ClassLoader.getSystemClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor().newInstance();
            return (T) new ByteBuddy()
                    .subclass(o.getClass())
                    .method(ElementMatchers.isDeclaredBy(o.getClass()))
                    .intercept(InvocationHandlerAdapter.of(new AOPProxyInterceptor(url, klass.getName())))
                    .make()
                    .load(ClassLoader.getSystemClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RpcfxException("build class", e);
        }
    }

    public static class AOPProxyInterceptor implements InvocationHandler {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private String url;

        private String targetClass;

        public AOPProxyInterceptor(String url, String targetClass) {
            this.url = url;
            this.targetClass = targetClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("bytebuddy delegate proxy2 before sing : " + method.getName());
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.targetClass);
            request.setMethod(method.getName());
            request.setParams(args);
            try {
                RpcfxResponse response = postNettyClient(request);
                if(!response.getStatus()) {
                    throw new RpcfxException("invoke failed", response.getException());
                }
                return JSON.parse(response.getResult().toString());
            } catch (Exception e){
                throw new RpcfxException("invoke rpc failed", e);
            }
        }

        private RpcfxResponse post(RpcfxRequest req) throws IOException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: "+reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(this.url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();
            System.out.println("resp json: "+respJson);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }


        private RpcfxResponse postNettyClient(RpcfxRequest req) {
            try {
                NettyClient nettyClient = newNettyClient();
                String reqJson = JSON.toJSONString(req);
                NettyHttpResponse nettyHttpResponse = nettyClient.doPost(this.url, reqJson);
                String body = nettyHttpResponse.getBody();
                return JSON.parseObject(body, RpcfxResponse.class);
            } catch (Exception e) {
                throw new RpcfxException(e);
            }
        }

        private NettyClient newNettyClient() {
            String urlTarget = this.url;
            urlTarget = urlTarget.startsWith("https://") ? urlTarget.substring(8) : urlTarget;
            urlTarget = urlTarget.startsWith("http://") ? urlTarget.substring(7) : urlTarget;
            String[] split = urlTarget.split("/");
            NettyClient nettyClient = new NettyClient(new HostAndPortConfig(split[0], Integer.parseInt(split[1])));
            nettyClient.initConnection();
            return nettyClient;
        }

    }





}
