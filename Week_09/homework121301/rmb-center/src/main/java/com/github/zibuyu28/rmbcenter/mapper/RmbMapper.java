package com.github.zibuyu28.rmbcenter.mapper;

import com.github.zibuyu28.rmbcenter.dao.RmbDao;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface RmbMapper {

    @Select("select id,user_id, rmb, freeze from t_rmb where user_id =#{userID} limit 1")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "user_id", column = "userId"),
            @Result(property = "rmb", column = "rmb"),
            @Result(property = "freeze", column = "freeze")
    })
    RmbDao getRmdInfoByUserID(long userID);


    @Update("update t_rmb set rmb = rmb - #{freezeAmount}, freeze = freeze + #{freezeAmount} where user_id = #{userId} and (rmb - #{freezeAmount}) >= 0")
    int tryFreeze(BigDecimal freezeAmount, long userId);

    @Update("update t_rmb set freeze = freeze - #{freezeAmount} where user_id = #{userId} and (freeze - #{freezeAmount}) >= 0")
    int confirm(BigDecimal freezeAmount, long userId);

    @Update("update t_rmb set rmb = rmb + #{freezeAmount}, freeze = freeze - #{freezeAmount} where user_id = #{userId} and (freeze - #{freezeAmount}) >= 0")
    int cancel(BigDecimal freezeAmount, long userId);

    @Update("update t_rmb set rmb = rmb + #{incomeAmount} where user_id = #{userId}")
    int income(BigDecimal incomeAmount, long userId);

    @Update("update t_rmb set rmb = rmb - #{incomeAmount} where user_id = #{userId} and (rmb - #{incomeAmount}) >= 0")
    int incomeCancel(BigDecimal incomeAmount, long userId);
}
