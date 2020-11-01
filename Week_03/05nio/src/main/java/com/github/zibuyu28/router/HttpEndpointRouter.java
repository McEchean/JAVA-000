package com.github.zibuyu28.router;

import java.util.List;

public interface HttpEndpointRouter {
    
//    String route(List<String> endpoints);

    String getEndPoint() throws Exception;
}
