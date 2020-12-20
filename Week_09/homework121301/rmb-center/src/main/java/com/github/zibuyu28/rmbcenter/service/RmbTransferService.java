package com.github.zibuyu28.rmbcenter.service;

import org.dromara.hmily.annotation.Hmily;

import java.math.BigDecimal;

public interface RmbTransferService {

    @Hmily
    public void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount);

    @Hmily
    public void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount);

}
