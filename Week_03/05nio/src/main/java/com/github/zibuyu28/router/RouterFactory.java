package com.github.zibuyu28.router;

import com.github.zibuyu28.util.Prop;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouterFactory {
    private static final Logger log = LoggerFactory.getLogger(RouterFactory.class);

    @Autowired
    private RandomEndpointRouter randomEndpointRouter;

    public HttpEndpointRouter getRouter() {
        RouterType rt = RouterType.parseType(Prop.getOrDefault("router.type", RouterType.Random.getName()));
        switch (rt) {
            case Random:
                return this.randomEndpointRouter;
            default:
                log.error("router type({}) not support now",rt);
                return null;
        }
    }
}
