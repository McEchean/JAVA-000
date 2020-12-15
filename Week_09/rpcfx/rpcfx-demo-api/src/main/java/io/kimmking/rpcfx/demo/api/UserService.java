package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.client.ARpcfx;

public interface UserService {

    @ARpcfx
    default User findById(int id) {
        System.out.println("hello");
        return null;
    }


}
