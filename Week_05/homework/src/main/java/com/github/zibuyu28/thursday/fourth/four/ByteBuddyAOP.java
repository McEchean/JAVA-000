package com.github.zibuyu28.thursday.fourth.four;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Field;

public class ByteBuddyAOP {
    public static void main(String[] args) throws Exception {
        Foo foo = new ByteBuddy()
                .subclass(Foo.class)
                .method(ElementMatchers.any())
                .intercept(Advice.to(LogIntercept.class))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded()
                .newInstance();

        Class<? extends Foo> aClass = foo.getClass();
        Field name = aClass.getSuperclass().getDeclaredField("name");
        name.setAccessible(true);
        name.set(foo,"hello");
        foo.say(1);
        foo.work(1);
    }
}
