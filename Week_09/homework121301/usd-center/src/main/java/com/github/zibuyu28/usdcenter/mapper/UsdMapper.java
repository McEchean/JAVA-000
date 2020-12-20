package com.github.zibuyu28.usdcenter.mapper;

import com.github.zibuyu28.usdcenter.dao.UsdDao;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface UsdMapper {

    @Select("select id,user_id, usd, freeze from t_usd where user_id =#{userID} limit 1")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "user_id", column = "userId"),
            @Result(property = "usd", column = "usd"),
            @Result(property = "freeze", column = "freeze")
    })
    UsdDao getUsdInfoByUserID(long userID);


    @Update("update t_usd set usd = usd - #{freezeAmount}, freeze = freeze + #{freezeAmount} where user_id = #{userId} and (usd - #{freezeAmount}) >= 0")
    int tryFreeze(BigDecimal freezeAmount, long userId);

    @Update("update t_usd set freeze = freeze - #{freezeAmount} where user_id = #{userId} and (freeze - #{freezeAmount}) >= 0")
    int confirm(BigDecimal freezeAmount, long userId);

    @Update("update t_usd set usd = usd + #{freezeAmount}, freeze = freeze - #{freezeAmount} where user_id = #{userId} and (freeze - #{freezeAmount}) >= 0")
    int cancel(BigDecimal freezeAmount, long userId);

    @Update("update t_usd set usd = usd + #{incomeAmount} where user_id = #{userId}")
    int income(BigDecimal incomeAmount, long userId);

    @Update("update t_usd set usd = usd - #{incomeAmount} where user_id = #{userId} and (usd - #{incomeAmount}) >= 0")
    int incomeCancel(BigDecimal incomeAmount, long userId);
}
