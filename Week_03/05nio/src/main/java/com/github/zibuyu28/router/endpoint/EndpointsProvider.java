package com.github.zibuyu28.router.endpoint;

import java.util.List;

public interface EndpointsProvider {
    List<String> availableEndpoints() throws Exception;
}
