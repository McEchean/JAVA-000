package com.github.zibuyu28.router.endpoint;

import com.github.zibuyu28.util.Prop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndpointProviderFactory {
    private static final Logger log = LoggerFactory.getLogger(EndpointProviderFactory.class);

    public static EndpointsProvider getProvider() {
        EndpointProviderType rt = EndpointProviderType.parseType(Prop.getOrDefault("router.endpoints.type", EndpointProviderType.DEFAULT.getName()));
        switch (rt) {
            case DEFAULT:
                return DefaultEndpointsProvider.getInstance();
            case ETCD:
                return EtcdConfigEndpointsProvider.getInstance();
            default:
                log.error("endpoint provider type({}) not support now",rt);
                return null;
        }
    }
}
