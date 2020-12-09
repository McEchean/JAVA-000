package com.github.zibuyu28.homework120301.controller;

import com.github.zibuyu28.homework120301.dao.OrderDao;
import com.github.zibuyu28.homework120301.service.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

@RestController
public class OrderController {
    @Autowired
    private OrderImpl order;

    @PatchMapping("/insert")
    public ResponseEntity<String> testInsert() {
        order.insetBatchMain();
        return new ResponseEntity<>("execute ok",HttpStatus.OK);
    }

    @GetMapping("/query/userid/{userID}/id/{id}")
    public ResponseEntity<OrderDao> testQuery(@PathVariable long id, @PathVariable long userID) {
        OrderDao orderDao = order.queryByID(id,userID);
        return new ResponseEntity<>(orderDao, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> testDelete(@PathVariable long id) {
        order.delete(id);
        return new ResponseEntity<>("delete ok",HttpStatus.OK);
    }

    @PatchMapping("/insertone")
    public ResponseEntity<String> testInsertOne() {
        OrderDao orderDao = new OrderDao();
        orderDao.setUuid(UUID.randomUUID().toString());
        orderDao.setUserId(15);
        orderDao.setProductId(2);
        orderDao.setProductCount(3);
        orderDao.setShippingFee(new BigDecimal(String.valueOf("6.00")));
        orderDao.setSumFee(new BigDecimal(String.valueOf(12 * 1 + 6)));
        orderDao.setRealFee(orderDao.getSumFee().subtract(new BigDecimal("30")));
        orderDao.setState(1);
        orderDao.setOrderTime(new Date(System.currentTimeMillis()));
        orderDao.setPayTime(new Date(System.currentTimeMillis()));
        orderDao.setDealTime(new Date(System.currentTimeMillis()));
        orderDao.setAddressId(1);
        orderDao.setSnapshotId(3);
        order.insert(orderDao);
        return new ResponseEntity<>("execute ok",HttpStatus.OK);
    }


}
