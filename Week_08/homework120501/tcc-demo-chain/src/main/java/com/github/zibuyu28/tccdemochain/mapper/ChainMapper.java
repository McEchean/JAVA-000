package com.github.zibuyu28.tccdemochain.mapper;
import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import org.apache.ibatis.annotations.*;

public interface ChainMapper {

    @Update("update chain set state = #{state}, message = #{message} where id = #{id}")
    int update(ChainStatus chain);

    @Insert("insert into chain (id, state, message) values (#{id}, #{state}, #{message})")
    void insert(ChainStatus chain);

    @Update("update chain set state = #{state}, message = #{message} where id = #{id}")
    int confirm(ChainStatus chain);

    @Update("update chain set state = #{state}, message = #{message} where id = #{id}")
    int cancel(ChainStatus chain);

    @Select("select id,state,message from chain where id =#{id} limit 1")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "state", column = "state"),
            @Result(property = "message", column = "message")
    })
    ChainStatus findChainByID(Long ID);
}
