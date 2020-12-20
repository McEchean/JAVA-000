package com.github.zibuyu28.usdcenter.service;

import org.dromara.hmily.annotation.Hmily;

import java.math.BigDecimal;

public interface UsdTransferService {

    @Hmily
    public void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount);

    @Hmily
    public void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount);
}
