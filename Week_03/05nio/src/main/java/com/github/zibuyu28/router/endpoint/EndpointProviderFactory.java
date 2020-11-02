package com.github.zibuyu28.router.endpoint;

import com.github.zibuyu28.util.Prop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointProviderFactory {
    private static final Logger log = LoggerFactory.getLogger(EndpointProviderFactory.class);

    public static EndpointsProvider newProvider() {
        EndpointProviderType rt = EndpointProviderType.parseType(Prop.getOrDefault("router.endpoints.type", EndpointProviderType.DEFAULT.getName()));
        switch (rt) {
            case DEFAULT:
                return new DefaultEndpointsProvider();
            case ETCD:
                return new EtcdConfigEndpointsProvider();
            default:
                log.error("endpoint provider type({}) not support now",rt);
                return null;
        }
    }
}
