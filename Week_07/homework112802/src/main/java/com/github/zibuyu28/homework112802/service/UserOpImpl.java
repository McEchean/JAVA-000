package com.github.zibuyu28.homework112802.service;

import com.github.zibuyu28.homework112802.dao.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@Service
public class UserOpImpl implements UserOp{
    @Resource
    private DataSource dataSource;

    @Override
    public User getUserById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.setDataSource();
        RowMapper<User> userRowMapper = (resultSet, i) -> new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getInt(3));
        List<User> query = jdbcTemplate.query(String.format("select * from user where id = %d", id), userRowMapper);
        return query.get(0);
    }

    @Override
    public void addUser(User user) {
        String sql = "insert into user values(?,?,?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, user.getId(),user.getName(),user.getAge());
    }
}
