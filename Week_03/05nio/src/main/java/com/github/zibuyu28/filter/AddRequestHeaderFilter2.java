package com.github.zibuyu28.filter;


import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.HttpRequest;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AddRequestHeaderFilter2 implements MethodBeforeAdvice {


    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        String name = method.getName();
        if(!name.equals("handle")) {
            return;
        }
        Object object = objects[0];
        if(object instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) object;
            req.headers().add("nio","hello filter");
        }
    }
}
