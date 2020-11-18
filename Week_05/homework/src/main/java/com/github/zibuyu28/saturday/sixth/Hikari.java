package com.github.zibuyu28.saturday.sixth;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hikari {

    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/blocface";
    private final static String USER = "root";
    private final static String PASSWORD = "admin123";

    private static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName(JDBC_DRIVER);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("idleTimeout","600000");
        config.addDataSourceProperty("maximumPoolSize","10");
        config.addDataSourceProperty("minimumIdle","2");
        config.addDataSourceProperty("connectionTimeout","30000");
        return new HikariDataSource(config);
    }

    private static List<AgentDao> query(int id, Connection conn) throws Exception {
        String querySQL = "select * from agent where id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AgentDao> agentDaos = new ArrayList<>();
        while (resultSet.next()) {
            AgentDao a = new AgentDao(resultSet.getInt(1),
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
            agentDaos.add(a);
        }
        return agentDaos;
    }

    private static void update(AgentDao agentDao, Connection conn) throws Exception {
        String updateSQL = "update agent set time_update = ?, uuid = ?, state = ?, message = ?, full_data = ?, machine_id = ?, resource_type = ?, addr = ? where id = ?";
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
    }

    private static void delete(int agentId, Connection conn) throws Exception {
        String deleteSQL = "delete from agent where id = ?";


        PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
        preparedStatement.setInt(1, agentId);

        preparedStatement.execute();
    }


    private static void insert(AgentDao agentDao, Connection conn) throws Exception {
        String insertSql = "INSERT INTO `agent` (`time_create`, `time_update`, `time_delete`, `uuid`, `state`, `message`, `full_data`, `machine_id`, `resource_type`, `addr`) " +
                "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement =  conn.prepareStatement(insertSql);
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
        HikariDataSource dataSource = getDataSource();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Hikari.insert(a, conn);
            a.setUuid(UUID.randomUUID().toString());
            a.setId(1);
            Hikari.update(a,conn);
            List<AgentDao> query = Hikari.query(a.getId(),conn);
            if(query.isEmpty())
                throw new RuntimeException("query empty");
            query.forEach(System.out::println);
            Hikari.delete(1,conn);
            conn.commit();
        }catch (Exception e) {
            e.printStackTrace();
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
