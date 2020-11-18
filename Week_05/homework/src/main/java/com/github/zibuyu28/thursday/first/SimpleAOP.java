package com.github.zibuyu28.thursday.first;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SimpleAOP implements InvocationHandler{

    private final Object target;

    public SimpleAOP(Object target) {
        this.target = target;
    }

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            before();
            Object o = method.invoke(this.target, args);
            after();
            return o;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void before() {
        System.out.println("before invoke");
    }

    public void after() {
        System.out.println("after invoke");
    }
}
