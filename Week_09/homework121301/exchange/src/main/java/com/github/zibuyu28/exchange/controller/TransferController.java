package com.github.zibuyu28.exchange.controller;


import com.github.zibuyu28.exchange.service.ExchangeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/exchange")
public class TransferController {
    @Autowired
    private ExchangeServiceImpl exchangeServiceImpl;

    @PostMapping("/usd2rmb/from/{fromUserID}/to/{toUserID}/amount/{amount}")
    public ResponseEntity<String> usd2rmb(@PathVariable long fromUserID, @PathVariable long toUserID, @PathVariable BigDecimal amount) {
        log.info("usd2rmb get param from : {}, to : {}, amount : {}", fromUserID, toUserID, amount);
        try {
            exchangeServiceImpl.usdTransferToRmb(fromUserID, toUserID, amount);
        }catch (Exception e) {
            return new ResponseEntity<>("not success", HttpStatus.OK);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/rmb2usd/from/{fromUserID}/to/{toUserID}/amount/{amount}")
    public ResponseEntity<String> rmb2usd(@PathVariable long fromUserID, @PathVariable long toUserID, @PathVariable BigDecimal amount) {
        log.info("rmb2usd get param from : {}, to : {}, amount : {}", fromUserID, toUserID, amount);
        try {
            exchangeServiceImpl.rmbTransferToUsd(fromUserID, toUserID, amount);
        }catch (Exception e) {
            return new ResponseEntity<>("not success", HttpStatus.OK);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
