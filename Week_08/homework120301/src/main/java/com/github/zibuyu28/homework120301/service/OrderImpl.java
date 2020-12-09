package com.github.zibuyu28.homework120301.service;

import com.github.zibuyu28.homework120301.dao.OrderDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
public class OrderImpl implements Order{
    @Autowired
    private DataSource dataSource;


    private Connection getConn() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insert(OrderDao orderDao) {
        Connection connection = getConn();
        String insertSql = "INSERT INTO `order` (`uuid`, `user_id`, `product_id`, `product_count`, `shipping_fee`, `sum_fee`, `real_fee`, `state`, `order_time`, `pay_time`, `deal_time`, `address_id`, `snapshot_id`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
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

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void delete(long orderID) {
        String deleteSQL = "delete from `order` where id = ?";
        Connection connection = getConn();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setLong(1, orderID);
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    @Override
    public void update(OrderDao orderDao) {
        String updateSQL = "update `order` set `uuid` = ?, `user_id` = ?, `product_id` = ?, `product_count` = ?, `shipping_fee` = ?, `sum_fee` = ?, `real_fee` = ?, `state` = ?, `order_time` = ?, `pay_time` = ?, `deal_time` = ?, `address_id` = ?, `snapshot_id` = ? where id = ?";
        Connection connection = getConn();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(updateSQL);
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
            preparedStatement.setLong(14, orderDao.getId());
            preparedStatement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public OrderDao queryByID(long orderID, long userID) {
        log.info("userid {}, id {}", userID, orderID);
        String querySQL = "select * from `order` where user_id = ? and id = ?";
        Connection connection = getConn();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setLong(1, userID);
            preparedStatement.setLong(2, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<OrderDao> orderDaos = new ArrayList<>();
            while (resultSet.next()) {
                OrderDao a = new OrderDao(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getLong(3),
                        resultSet.getLong(4),
                        resultSet.getInt(5),
                        resultSet.getBigDecimal(6),
                        resultSet.getBigDecimal(7),
                        resultSet.getBigDecimal(8),
                        resultSet.getInt(9),
                        resultSet.getDate(10),
                        resultSet.getDate(11),
                        resultSet.getDate(12),
                        resultSet.getLong(13),
                        resultSet.getLong(14),
                        resultSet.getInt(15),
                        resultSet.getDate(16),
                        resultSet.getDate(17));
                orderDaos.add(a);
            }
            if(orderDaos.size() == 0) {
                return null;
            }
            return orderDaos.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insetBatchMain() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            ArrayList<OrderDao> list = new ArrayList<>(100000);
            for (int j = 0; j < 100000; j++) {
                OrderDao orderDao = new OrderDao();
                orderDao.setUuid(UUID.randomUUID().toString());
                orderDao.setUserId(j);
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
            log.info("insert one batch time : {}" ,(System.currentTimeMillis() - insertStart));
        }

        log.info("insert batch time : {}", (System.currentTimeMillis() - start));
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
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
