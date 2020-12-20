package com.github.zibuyu28.exchange.service;

import java.math.BigDecimal;

public interface ExchangeService {
    void rmbTransferToUsd(long fromUserID, long toUserID, BigDecimal amount);
    void usdTransferToRmb(long fromUserID, long toUserID, BigDecimal amount);
}
