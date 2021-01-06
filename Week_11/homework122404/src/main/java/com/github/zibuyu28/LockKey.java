package com.github.zibuyu28;

public enum LockKey {
    GLOBAL_LOCK("global::key");

    private String key;

    private LockKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
