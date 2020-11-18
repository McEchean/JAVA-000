package com.github.zibuyu28.saturday.sixth;

import java.sql.*;
import java.util.UUID;

public class JDBC1 {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/blocface";
    private final static String USER = "root";
    private final static String PASSWORD = "admin123";
    private static Connection conn;


    private synchronized static Connection getConn() {
        if(conn == null) {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    private static AgentDao query(int id) {
        String querySQL = "select * from agent where id = ?";
        Connection conn = getConn();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                return new AgentDao(resultSet.getInt(1),
                        resultSet.getDate(2),
                        resultSet.getDate(3),
                        resultSet.getDate(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getString(10),
                        resultSet.getString(11));
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void update(AgentDao agentDao) {
        String updateSQL = "update agent set time_update = ?, uuid = ?, state = ?, message = ?, full_data = ?, machine_id = ?, resource_type = ?, addr = ? where id = ?";
        Connection conn = getConn();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
            preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
            preparedStatement.setString(2, agentDao.getUuid());
            preparedStatement.setInt(3, agentDao.getState());
            preparedStatement.setString(4, agentDao.getMessage());
            preparedStatement.setString(5, agentDao.getFullData());
            preparedStatement.setString(6, agentDao.getMachineID());
            preparedStatement.setString(7, agentDao.getResourceType());
            preparedStatement.setString(8, agentDao.getAddr());
            preparedStatement.setInt(9, agentDao.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void delete(int agentId) {
        String deleteSQL = "delete from agent where id = ?";

        Connection conn = getConn();
        try {

            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, agentId);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void insert(AgentDao agentDao) {
        try {
            Connection connection = getConn();
            String insertSql = "INSERT INTO `agent` (`time_create`, `time_update`, `time_delete`, `uuid`, `state`, `message`, `full_data`, `machine_id`, `resource_type`, `addr`) " +
                    "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
            preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
            preparedStatement.setString(3, agentDao.getUuid());
            preparedStatement.setInt(4, agentDao.getState());
            preparedStatement.setString(5, agentDao.getMessage());
            preparedStatement.setString(6, agentDao.getFullData());
            preparedStatement.setString(7, agentDao.getMachineID());
            preparedStatement.setString(8, agentDao.getResourceType());
            preparedStatement.setString(9, agentDao.getAddr());

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AgentDao a = new AgentDao();
        a.setUuid(UUID.randomUUID().toString());
        a.setState(3);
        a.setMessage("init");
        a.setFullData("this is full data");
        a.setMachineID("1");
        a.setResourceType("host");
        a.setAddr("hostip:port");
        JDBC1.insert(a);
        a.setUuid(UUID.randomUUID().toString());
        a.setId(1);
        JDBC1.update(a);
        AgentDao query = JDBC1.query(a.getId());
        System.out.println(query.toString());
        JDBC1.delete(1);
    }

}
