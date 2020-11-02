package com.github.zibuyu28.router.endpoint;

import java.util.HashMap;

public enum EndpointProviderType {
    DEFAULT("default"),
    ETCD("etcd");

    private final String name;

    private static HashMap<String, EndpointProviderType> m = new HashMap<>();

    static {
        for (EndpointProviderType t : EndpointProviderType.values()) {
            m.put(t.name, t);
        }
    }

    EndpointProviderType(String httpclient) {
        this.name = httpclient;
    }

    public static EndpointProviderType parseType(String name) {
        return m.get(name);
    }

    public String getName() {
        return name;
    }
}
