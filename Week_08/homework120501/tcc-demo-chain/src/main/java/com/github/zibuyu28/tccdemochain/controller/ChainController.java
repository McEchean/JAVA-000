package com.github.zibuyu28.tccdemochain.controller;


import com.github.zibuyu28.tccdemochain.service.ChainService;
import com.github.zibuyu28.tccdemochain.service.ChainServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chain")
@Slf4j
public class ChainController {

    @Autowired
    private ChainService chainService;

    @PostMapping("/update/success/{success}")
    public ResponseEntity<String> configChain(@PathVariable boolean success) {
        chainService.updateConfig(success);
        log.info("{}", success);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
