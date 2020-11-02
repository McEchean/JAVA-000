package com.github.zibuyu28.router.endpoint;

import com.github.zibuyu28.util.Prop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultEndpointsProvider implements EndpointsProvider {

    @Override
    public List<String> availableEndpoints() throws Exception {
        String hostsv = Prop.getOrDefault("router.endpoints.default.hosts");
        return new ArrayList<>(Arrays.stream(hostsv.split(",")).collect(Collectors.toList()));
    }
}
