package com.github.zibuyu28.router;

import java.util.HashMap;

public enum RouterType {
    Random("random"),
    RoundRobin("roundRobin"),
    Weight("weight");

    private final String name;

    private static HashMap<String, RouterType> m = new HashMap<>();

    static {
        for (RouterType t : RouterType.values()) {
            m.put(t.name, t);
        }
    }

    RouterType(String httpclient) {
        this.name = httpclient;
    }

    public static RouterType parseType(String name) {
        return m.get(name);
    }

    public String getName() {
        return name;
    }
}
