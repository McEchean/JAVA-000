package com.github.zibuyu28;

import com.github.zibuyu28.dao.OrderDao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InsertDataJDBC {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/blocface";
    private final static String USER = "root";
    private final static String PASSWORD = "admin123";
    private ArrayBlockingQueue<Connection> connections = new ArrayBlockingQueue<Connection>(20);
    private ThreadPoolExecutor executor;

    private void init() {
        int core = Runtime.getRuntime().availableProcessors() * 4;
        int timeout = 1000;
        int size = 1000;
        executor = new ThreadPoolExecutor(core, core,
                timeout, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(size), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 20; i++) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                connection.setAutoCommit(false);
                connections.put(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConn() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void close(Connection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void insertData() {
        insertOneByOne();
        insetBatchMain();
    }

    private void insetBatchMain() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            ArrayList<OrderDao> list = new ArrayList<>(100000);
            for (int j = 0; j < 100000; j++) {
                OrderDao orderDao = new OrderDao();
                orderDao.setUuid(UUID.randomUUID().toString());
                orderDao.setUserId(1);
                orderDao.setProductId(i);
                orderDao.setProductCount(i);
                orderDao.setShippingFee(new BigDecimal(String.valueOf("6.00")));
                orderDao.setSumFee(new BigDecimal(String.valueOf(12 * i + 6)));
                orderDao.setRealFee(orderDao.getSumFee().subtract(new BigDecimal("30")));
                orderDao.setState(1);
                orderDao.setOrderTime(new Date(System.currentTimeMillis()));
                orderDao.setPayTime(new Date(System.currentTimeMillis()));
                orderDao.setDealTime(new Date(System.currentTimeMillis()));
                orderDao.setAddressId(1);
                orderDao.setSnapshotId(i);
                list.add(orderDao);
            }
            long insertStart = System.currentTimeMillis();
            insertBatch(list);
            System.out.println("insert one batch time : " + (System.currentTimeMillis() - insertStart));
        }

        System.out.println("insert batch time : " + (System.currentTimeMillis() - start));
    }

    private void insertBatch(ArrayList<OrderDao> orders) {
        Connection connection = getConn();
        try {
            String insertSql = "INSERT INTO `order` (`uuid`, `user_id`, `product_id`, `product_count`, `shipping_fee`, `sum_fee`, `real_fee`, `state`, `order_time`, `pay_time`, `deal_time`, `address_id`, `snapshot_id`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            orders.forEach(orderDao -> {
                try {
                    preparedStatement.setString(1, orderDao.getUuid());
                    preparedStatement.setLong(2, orderDao.getUserId());
                    preparedStatement.setLong(3, orderDao.getProductId());
                    preparedStatement.setInt(4, orderDao.getProductCount());
                    preparedStatement.setBigDecimal(5, orderDao.getShippingFee());
                    preparedStatement.setBigDecimal(6, orderDao.getSumFee());
                    preparedStatement.setBigDecimal(7, orderDao.getRealFee());
                    preparedStatement.setInt(8, orderDao.getState());
                    preparedStatement.setDate(9, new Date(orderDao.getOrderTime().getTime()));
                    preparedStatement.setDate(10, new Date(orderDao.getPayTime().getTime()));
                    preparedStatement.setDate(11, new Date(orderDao.getDealTime().getTime()));
                    preparedStatement.setLong(12, orderDao.getAddressId());
                    preparedStatement.setLong(13, orderDao.getSnapshotId());
                    preparedStatement.addBatch();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
            preparedStatement.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            close(connection);
        }
    }

    private void insertOneByOne() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            OrderDao orderDao = new OrderDao();
            orderDao.setUuid(UUID.randomUUID().toString());
            orderDao.setUserId(1);
            orderDao.setProductId(i);
            orderDao.setProductCount(i);
            orderDao.setShippingFee(new BigDecimal(String.valueOf("6.00")));
            orderDao.setSumFee(new BigDecimal(String.valueOf(12 * i + 6)));
            orderDao.setRealFee(orderDao.getSumFee().subtract(new BigDecimal("30")));
            orderDao.setState(1);
            orderDao.setOrderTime(new Date(System.currentTimeMillis()));
            orderDao.setPayTime(new Date(System.currentTimeMillis()));
            orderDao.setDealTime(new Date(System.currentTimeMillis()));
            orderDao.setAddressId(1);
            orderDao.setSnapshotId(i);
            insert(orderDao);
        }
        System.out.println("insert one by one time : " + (System.currentTimeMillis() - start));
    }

    private void insert(OrderDao orderDao) {
        Connection connection = getConn();
        try {
            String insertSql = "INSERT INTO `order` (`uuid`, `user_id`, `product_id`, `product_count`, `shipping_fee`, `sum_fee`, `real_fee`, `state`, `order_time`, `pay_time`, `deal_time`, `address_id`, `snapshot_id`) " +
                    "VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, orderDao.getUuid());
            preparedStatement.setLong(2, orderDao.getUserId());
            preparedStatement.setLong(3, orderDao.getProductId());
            preparedStatement.setInt(4, orderDao.getProductCount());
            preparedStatement.setBigDecimal(5, orderDao.getShippingFee());
            preparedStatement.setBigDecimal(6, orderDao.getSumFee());
            preparedStatement.setBigDecimal(7, orderDao.getRealFee());
            preparedStatement.setInt(8, orderDao.getState());
            preparedStatement.setDate(9, new Date(orderDao.getOrderTime().getTime()));
            preparedStatement.setDate(10, new Date(orderDao.getPayTime().getTime()));
            preparedStatement.setDate(11, new Date(orderDao.getDealTime().getTime()));
            preparedStatement.setLong(12, orderDao.getAddressId());
            preparedStatement.setLong(13, orderDao.getSnapshotId());

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    public static void main(String[] args) {
        InsertDataJDBC insertDataJDBC = new InsertDataJDBC();
        insertDataJDBC.init();
        insertDataJDBC.insertData();
    }
}
