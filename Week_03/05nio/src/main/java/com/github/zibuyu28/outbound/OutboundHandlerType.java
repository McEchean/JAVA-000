package com.github.zibuyu28.outbound;

import java.util.HashMap;

public enum OutboundHandlerType {
    NettyClient("nettyclient"),
    HttpClient4("httpclient"),
    OkHttpClient("okhttpclient");

    private final String name;

    private static HashMap<String, OutboundHandlerType> m = new HashMap<>();

    static {
        for (OutboundHandlerType t :OutboundHandlerType.values()) {
            m.put(t.name, t);
        }
    }

    OutboundHandlerType(String httpclient) {
        this.name = httpclient;
    }

    public static OutboundHandlerType parseType(String name) {
        return m.get(name);
    }

    public String getName() {
        return name;
    }
}
