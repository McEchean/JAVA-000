package com.github.zibuyu28.tccdemonode.service;

import com.github.zibuyu28.tccdemonode.dao.Node;
import org.dromara.hmily.annotation.Hmily;

public interface OpNode {

   @Hmily
   boolean UpdateConfig(Node node);

}
