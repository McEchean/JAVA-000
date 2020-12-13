package com.github.zibuyu28.tccdemochain.service;

import com.github.zibuyu28.chain.service.Chain;
import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChainServiceImpl implements ChainService{

    private final TempService tempService;

    public ChainServiceImpl(TempService tempService) {
        this.tempService = tempService;
    }

    @Override
    public void updateConfig(boolean success) {
        tempService.updateConfig(success);
    }
}
