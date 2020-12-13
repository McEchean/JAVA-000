package com.github.zibuyu28.tccdemochain.service;

import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import com.github.zibuyu28.tccdemochain.mapper.ChainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
public class ChainStateServcie implements ChainState {

    @Autowired
    private ChainMapper chainMapper;
//
    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean update(ChainStatus chain) {
        return chainMapper.update(chain) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(ChainStatus accountDTO) {
        log.info("============grpc chain tcc 执行确认接口===============");
        chainMapper.confirm(accountDTO);
        return Boolean.TRUE;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(ChainStatus chain) {
        log.info("============ grpc chain tcc 执行取消接口===============");
        final ChainStatus chainStatusFind = chainMapper.findChainByID(chain.getId());
        if(chainStatusFind != null) {
            chainMapper.cancel(chain);
        }
        return Boolean.TRUE;
    }

}
