package com.github.zibuyu28.agent;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AdviceService {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        System.out.println("拦截的方法名称 : " + method.getName());
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        }finally {
            System.out.println("AGENT 拦截 SERVICE 花费时间:" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
