package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.client.ARpcfx;

public interface OrderService {

    @ARpcfx
    default Order findOrderById(Integer id) {
        return null;
    }

}
