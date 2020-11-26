package com.github.zibuyu28.thursday.fourth.four;

@Log
public class Foo {
    private String name;

    @Log
    public int say(int v) {
        System.out.println(String.format("%s said you are foo", this.name));
        return 1;
    }

    @Log
    public int work(int v) {
        System.out.println(String.format("%s is working", this.name));
        return 1;
    }
}
