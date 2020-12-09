package com.github.zibuyu28.homework120301.service;

import com.github.zibuyu28.homework120301.dao.OrderDao;

public interface Order {
    void insert(OrderDao orderDao);
    void delete(long orderID);
    void update(OrderDao orderDao);
    OrderDao queryByID(long orderID, long userID);
}
