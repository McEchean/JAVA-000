package com.github.zibuyu28.router;

import com.github.zibuyu28.util.Prop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterFactory {
    private static final Logger log = LoggerFactory.getLogger(RouterFactory.class);

    public static HttpEndpointRouter newRouter() {
        RouterType rt = RouterType.parseType(Prop.getOrDefault("router.type", RouterType.Random.getName()));
        switch (rt) {
            case Random:
                return RandomEndpointRouter.getInstance();
            default:
                log.error("router type({}) not support now",rt);
                return null;
        }
    }
}
