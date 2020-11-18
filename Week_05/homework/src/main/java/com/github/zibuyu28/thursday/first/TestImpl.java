package com.github.zibuyu28.thursday.first;

import java.lang.reflect.Proxy;

public class TestImpl implements Test {
    @Override
    public void test() {
        System.out.println("real method exec");
    }

    public static void main(String[] args) {
        TestImpl t = new TestImpl();

        SimpleAOPAd simpleAOP = new SimpleAOPAd(t);

        Test test = (Test) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), simpleAOP);
        test.test();
    }
}
