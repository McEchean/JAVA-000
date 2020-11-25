package com.github.zibuyu28.thursday.fourth.four;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LogIntercept{
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
        if (method.getAnnotation(Log.class) != null) {
            System.out.println(String.format("enter %s, args is %s", method.getName(), Arrays.toString(arguments)));
        }
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
        if (method.getAnnotation(Log.class) != null) {
            System.out.println(String.format("exit %s, args is %s, return is %S", method.getName(), Arrays.toString(arguments),ret));
        }
    }
}
