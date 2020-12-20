package com.github.zibuyu28.exchange.service;


import com.github.zibuyu28.rmbcenter.service.RmbTransferService;
import com.github.zibuyu28.usdcenter.service.UsdTransferService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @DubboReference(version = "1.0.0")
    private RmbTransferService rmbTransferService;

    @DubboReference(version = "1.0.0")
    private UsdTransferService usdTransferService;

    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount) {
        rmbTransferService.rmbTransferToUsd(fromUserID, toUserID, amount);
        usdTransferService.rmbTransferToUsd(toUserID,fromUserID,amount.divide(new BigDecimal("7"),2, RoundingMode.HALF_UP));
        usdTransferService.usdTransferToRmb(fromUserID,toUserID, amount.divide(new BigDecimal("7"),2, RoundingMode.HALF_UP));
        rmbTransferService.usdTransferToRmb(toUserID, fromUserID, amount);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirm(long fromUserID, long toUserID, BigDecimal amount) {
        System.out.println("成功交易");
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(long fromUserID, long toUserID, BigDecimal amount) {
        System.out.println("取消了交易");
    }

    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount) {
        usdTransferService.usdTransferToRmb(fromUserID, toUserID, amount);
        rmbTransferService.rmbTransferToUsd(toUserID, fromUserID, amount.multiply(new BigDecimal("7")));
        rmbTransferService.usdTransferToRmb(fromUserID, toUserID, amount.multiply(new BigDecimal("7")));
        usdTransferService.rmbTransferToUsd(toUserID, fromUserID, amount);
    }



}
