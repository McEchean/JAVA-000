package com.github.zibuyu28.homework112801.service;

import com.github.zibuyu28.homework112801.config.AnnoDataSource;
import com.github.zibuyu28.homework112801.config.MultiDatasource;
import com.github.zibuyu28.homework112801.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class UserOpImpl implements UserOp{
//    private JdbcTemplate masterJdbcTemplate;
//    private JdbcTemplate slaveJdbcTemplate;
//
//
//    public UserOpImpl(DataSource master, DataSource slave) {
//        this.masterJdbcTemplate = new JdbcTemplate(master);
//        this.slaveJdbcTemplate = new JdbcTemplate(slave);
//    }
    @Autowired
    private MultiDatasource multiDatasource;

    @Override
    @AnnoDataSource("slave")
    public User getUserById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(multiDatasource);
//        jdbcTemplate.setDataSource();
        RowMapper<User> userRowMapper = (resultSet, i) -> new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getInt(3));
        List<User> query = jdbcTemplate.query(String.format("select * from user where id = %d", id), userRowMapper);
        return query.get(0);
    }

    @Override
    @AnnoDataSource("master")
    public void addUser(User user) {
        String sql = "insert into user values(?,?,?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(multiDatasource);
        jdbcTemplate.update(sql, user.getId(),user.getName(),user.getAge());
    }
}
