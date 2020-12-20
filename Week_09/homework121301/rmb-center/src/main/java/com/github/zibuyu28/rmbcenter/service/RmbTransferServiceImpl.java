package com.github.zibuyu28.rmbcenter.service;

import com.github.zibuyu28.rmbcenter.mapper.RmbMapper;
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
public class RmbTransferServiceImpl implements RmbTransferService {

    @Autowired
    private RmbMapper rmbMapper;

    @Override
    @HmilyTCC(confirmMethod = "rmbTransferConfirm", cancelMethod = "rmbTransferCancel")
    public void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount) {
        int i = rmbMapper.tryFreeze(amount, fromUserID);
        if(i <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== rmb center : try freeze success ====");

    }

    @Transactional(rollbackFor = Exception.class)
    public void rmbTransferConfirm(long fromUserID, long toUserID, BigDecimal amount) {
        int confirm = rmbMapper.confirm(amount, fromUserID);
        if(confirm <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== rmb center : confirm result {} ====", confirm);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rmbTransferCancel(long fromUserID, long toUserID, BigDecimal amount) {
        int cancel = rmbMapper.cancel(amount, fromUserID);
        if(cancel <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== rmb center : cancel result {} ====", cancel);
    }

    @Override
    @HmilyTCC(confirmMethod = "usdTransferConfirm", cancelMethod = "usdTransferCancel")
    public void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount) {
        int income = rmbMapper.income(amount, fromUserID);
        if(income <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== rmb center : income result {} ====", income);
    }

    @Transactional(rollbackFor = Exception.class)
    public void usdTransferConfirm(long fromUserID, long toUserID, BigDecimal amount) {
        log.info("==== rmb center : rmb income success ====");
    }

    @Transactional(rollbackFor = Exception.class)
    public void usdTransferCancel(long fromUserID, long toUserID, BigDecimal amount) {
        int cancel = rmbMapper.incomeCancel(amount, fromUserID);
        if(cancel <= 0) {
            throw new RuntimeException("not success");
        }
        log.info("==== rmb center : rmb income cancel result {} ====", cancel);
    }

}
