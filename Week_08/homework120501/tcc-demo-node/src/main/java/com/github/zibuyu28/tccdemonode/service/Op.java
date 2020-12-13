package com.github.zibuyu28.tccdemonode.service;

public enum Op {
    UPDATE_CONFIG("updateConfig");

    private final String name;

    Op(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
