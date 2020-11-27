package com.github.zibuyu28.saturday.first;

public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {
        throw new RuntimeException("create not allowed");
    }

    public static Singleton getInstance() {
        return instance;
    }
}

// 适用于单线程环境
class Singleton2 {
    private static Singleton2 instance;

    private Singleton2() {
        throw new RuntimeException("create not allowed");
    }

    public static Singleton2 getInstance() {
        if(instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}

// 注意 volatile 关键字
class Singleton3 {
    private static volatile Singleton3 instance;

    private Singleton3() {
        throw new RuntimeException("create not allowed");
    }

    public static Singleton3 getInstance() {
        if(instance == null) {
            synchronized (Singleton3.class) {
                if(instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }
}

class Singleton4 {
    private static Singleton4 instance;

    private Singleton4() {
        throw new RuntimeException("create not allowed");
    }

    public synchronized static Singleton4 getInstance() {
        if(instance == null) {
            instance = new Singleton4();
        }
        return instance;
    }
}

class Singleton5 {
    private Singleton5() {
        throw new RuntimeException("create not allowed");
    }

    static class Factory {
        private static Singleton5 INSTANCE = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return Factory.INSTANCE;
    }
}

enum Singleton6 {
    INSTANCE("hh");

    private String name;

    Singleton6(String name) {
        this.name = name;
    }
}


