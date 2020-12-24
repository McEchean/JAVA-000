package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.client.ARpcfx;

public interface UserService {

    @ARpcfx
    default User findById(Integer id) {
        System.out.println("hello");
        return null;
    }


}
