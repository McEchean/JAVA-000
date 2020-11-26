package com.github.zibuyu28.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String arg, Instrumentation inst) {
        System.out.println("======= agent start ======");
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) ->
                builder.method(ElementMatchers.isMethod()
                .and(ElementMatchers.isAnnotatedWith(ElementMatchers.named("com.github.zibuyu28.thursday.fourth.four.Log"))))
                .intercept(MethodDelegation.to(AdviceService.class));
//        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) ->
//                builder.method(ElementMatchers.<MethodDescription>any())
//                        .intercept(MethodDelegation.to(AdviceService.class));

        AgentBuilder agentBuilder = new AgentBuilder
                .Default()
                .type(ElementMatchers.nameStartsWith("com.github.zibuyu28.thursday.fourth.four"))
//                .type(ElementMatchers.isAnnotatedWith(ElementMatchers.named("com.github.zibuyu28.thursday.fourth.four.Log")))
                .transform(transformer);

        agentBuilder.installOn(inst);
        System.out.println("======= agent end ======");
    }
}
