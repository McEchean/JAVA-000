package com.github.zibuyu28.tccdemonode.service;

import com.github.zibuyu28.tccdemonode.dao.Node;
import com.github.zibuyu28.tccdemonode.mapper.NodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OpNodeService implements OpNode{
    @Autowired
    private NodeMapper nodeMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean UpdateConfig(Node node) {
        if(node.getId() > 3) {
            throw new RuntimeException("节点 id 为 4");
        }
        log.info("节点id {}", node.getId());
        return nodeMapper.update(node) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(Node node) {
        log.info("============ grpc node tcc 执行确认接口===============");
        return nodeMapper.confirm(node) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Node node) {
        log.info("============ grpc node tcc 执行取消接口===============");
        final Node nodeTable = nodeMapper.findNodeByID(node.getId());
        if(nodeTable == null) {
            return Boolean.TRUE;
        }
        nodeTable.setConfig("配置失败");
        return nodeMapper.cancel(nodeTable) > 0;
    }
}
