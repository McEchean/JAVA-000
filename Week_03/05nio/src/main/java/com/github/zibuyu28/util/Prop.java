package com.github.zibuyu28.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Prop {
    private static final Logger log = LoggerFactory.getLogger(Prop.class);

    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(Prop.class.getClassLoader().getResourceAsStream("gateway.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Prop() {
        throw new RuntimeException("not allowed");
    }

    public static String getOrDefault(String key, String ... defaultV) throws RuntimeException {
        if(properties.containsKey(key)) {
            return (String)properties.get(key);
        }
        if(defaultV.length == 0) {
            throw new RuntimeException(String.format("key(%s) not exist",key));
        }
        log.debug("key({}) not exist,return default({})", key, defaultV[0]);
        return defaultV[0];
    }
}
