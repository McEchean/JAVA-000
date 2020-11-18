package com.github.zibuyu28.thursday.first;

public class SimpleAOPAd extends SimpleAOP {
    public SimpleAOPAd(Object target) {
        super(target);
    }

    @Override
    public void before() {
        System.out.println("Advance before");
    }

    @Override
    public void after() {
        System.out.println("Advance after");
    }


}
