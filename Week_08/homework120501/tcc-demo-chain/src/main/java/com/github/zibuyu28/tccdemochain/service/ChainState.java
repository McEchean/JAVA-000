package com.github.zibuyu28.tccdemochain.service;

import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import org.dromara.hmily.annotation.Hmily;

public interface ChainState {
    @Hmily
    boolean update(ChainStatus chain);
}
