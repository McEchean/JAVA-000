package com.github.zibuyu28.tccdemonode.mapper;

import com.github.zibuyu28.tccdemonode.dao.Node;
import org.apache.ibatis.annotations.*;

public interface NodeMapper {

    @Update("update node set config = #{config} where id = #{id}")
    int update(Node node);

    @Insert("insert into node (id,config) values (#{id}, #{config})")
    void insert(Node node);

    @Update("update node set config = #{config} where id = #{id}")
    int confirm(Node node);

    @Update("update node set config = #{config} where id = #{id}")
    int cancel(Node node);

    @Select("select id,config from node where id =#{id} limit 1")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "config", column = "config"),
    })
    Node findNodeByID(Long ID);
}
