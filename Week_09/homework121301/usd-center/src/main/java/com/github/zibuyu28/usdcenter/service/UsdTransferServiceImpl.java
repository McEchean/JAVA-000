package com.github.zibuyu28.usdcenter.service;

import com.github.zibuyu28.usdcenter.mapper.UsdMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Slf4j
@DubboService(version = "1.0.0")
@Service
public class UsdTransferServiceImpl implements UsdTransferService {
    @Autowired
    private UsdMapper usdMapper;

    @Override
    @HmilyTCC(confirmMethod = "usdTransferConfirm", cancelMethod = "usdTransferCancel")
    public void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount) {
        int i = usdMapper.tryFreeze(amount, fromUserID);
        if(i <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== usd center : try freeze result {} ====", i);
    }

    @Transactional(rollbackFor = Exception.class)
    public void usdTransferConfirm(long fromUserID, long toUserID, BigDecimal amount) {
        int confirm = usdMapper.confirm(amount, fromUserID);
        if(confirm <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== usd center : confirm result {} ====", confirm);
    }

    @Transactional(rollbackFor = Exception.class)
    public void usdTransferCancel(long fromUserID, long toUserID, BigDecimal amount) {
        int cancel = usdMapper.cancel(amount, fromUserID);
        if(cancel <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== usd center : cancel result {} ====", cancel);
    }

    @Override
    @HmilyTCC(confirmMethod = "rmbTransferConfirm", cancelMethod = "rmbTransferCancel")
    public void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount) {
        int income = usdMapper.income(amount, fromUserID);
        if(income <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== usd center : income result {} ====", income);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rmbTransferConfirm(long fromUserID, long toUserID, BigDecimal amount) {
        log.info("==== usd center : usd income confirm success ====");
    }

    @Transactional(rollbackFor = Exception.class)
    public void rmbTransferCancel(long fromUserID, long toUserID, BigDecimal amount) {
        int cancel = usdMapper.incomeCancel(amount, fromUserID);
        if(cancel <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== usd center : usd income cancel result {} ====", cancel);
    }

}
