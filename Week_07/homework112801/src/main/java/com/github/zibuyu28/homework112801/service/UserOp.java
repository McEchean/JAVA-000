package com.github.zibuyu28.homework112801.service;

import com.github.zibuyu28.homework112801.dao.User;

public interface UserOp {
    User getUserById(int id);
    void addUser(User user);
}
