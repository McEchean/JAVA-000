package com.github.zibuyu28.router.endpoint;

import java.util.ArrayList;
import java.util.List;

public class DefaultEndpoints implements Endpoints{

    @Override
    public List<String> availableEndpoints() throws Exception {
        ArrayList<String> eps = new ArrayList<>();

        eps.add("127.0.0.1:8088");

        return eps;
    }
}
