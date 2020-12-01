package com.github.zibuyu28.homework112802.service;

import com.github.zibuyu28.homework112802.dao.User;

public interface UserOp {
    User getUserById(int id);
    void addUser(User user);
}
