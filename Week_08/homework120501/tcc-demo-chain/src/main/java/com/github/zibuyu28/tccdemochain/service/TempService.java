package com.github.zibuyu28.tccdemochain.service;

import org.dromara.hmily.annotation.Hmily;

public interface TempService {
    @Hmily
    void updateConfig(boolean success);
}
