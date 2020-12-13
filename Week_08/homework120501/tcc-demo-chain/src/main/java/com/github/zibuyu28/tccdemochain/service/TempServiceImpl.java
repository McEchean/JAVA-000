package com.github.zibuyu28.tccdemochain.service;

import com.github.zibuyu28.chain.service.Chain;
import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import com.github.zibuyu28.tccdemochain.grpc.ChainStateClient;
import com.github.zibuyu28.tccdemochain.grpc.OpNodeClient;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TempServiceImpl implements TempService {
    @Autowired
    private OpNodeClient opNodeClient;

    @Autowired
    private ChainStateClient chainStateClient;


    @HmilyTCC(confirmMethod = "confirmConfig", cancelMethod = "cancelConfig")
    public void updateConfig(boolean success) {
        chainStateClient.updateChainState(1, 2, String.format("开始更新所有节点配置 %d", System.currentTimeMillis()));
        if(success) {
            opNodeClient.updateNodeConfig(3,String.format("节点 3 更新配置成功 %d", System.currentTimeMillis()));
        } else {
            opNodeClient.updateNodeConfig(4,String.format("节点 4 更新配置成功 %d", System.currentTimeMillis()));
        }
    }

    public void confirmConfig(boolean success) {
        log.info("chain confirm 完成");
    }

    public void cancelConfig(boolean success) {
        log.info("chain cancel 完成");
    }
}
