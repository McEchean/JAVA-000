package com.github.zibuyu28.router;

import com.github.zibuyu28.router.endpoint.EndpointProviderFactory;
import com.github.zibuyu28.router.endpoint.EndpointsProvider;
import com.github.zibuyu28.router.endpoint.EtcdConfigEndpointsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class RandomEndpointRouter implements HttpEndpointRouter {
    private static final Logger log = LoggerFactory.getLogger(RandomEndpointRouter.class);

    public static class F {
        private static RandomEndpointRouter INSTANCE = new RandomEndpointRouter();
    }

    public static RandomEndpointRouter getInstance() {
        return F.INSTANCE;
    }


    private final EndpointsProvider endpointsProvider;

    private RandomEndpointRouter() {
        this.endpointsProvider = EndpointProviderFactory.newProvider();
    }

    @Override
    public String getEndPoint() throws Exception {
        List<String> endpoint = endpointsProvider.availableEndpoints();
        if(endpoint.size() == 0) {
            throw new RuntimeException("no available endpoint");
        }
        Random random = new Random();
        int index = random.nextInt(endpoint.size());
        log.debug("random index {}", index);
        return endpoint.get(index);
    }
}
