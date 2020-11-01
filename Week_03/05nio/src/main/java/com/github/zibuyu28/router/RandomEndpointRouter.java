package com.github.zibuyu28.router;

import com.github.zibuyu28.router.endpoint.DefaultEndpoints;
import com.github.zibuyu28.router.endpoint.Endpoints;

import java.util.List;
import java.util.Random;

public class RandomEndpointRouter implements HttpEndpointRouter {
    private final Endpoints endpoints;

    public RandomEndpointRouter() {
        this.endpoints = new DefaultEndpoints();
    }

    @Override
    public String getEndPoint() throws Exception {
        List<String> endpoint = endpoints.availableEndpoints();
        if(endpoint.size() == 0) {
            throw new RuntimeException("no available endpoint");
        }
        Random random = new Random();
        int index = random.nextInt(endpoint.size());
        return endpoint.get(index);
    }
}
