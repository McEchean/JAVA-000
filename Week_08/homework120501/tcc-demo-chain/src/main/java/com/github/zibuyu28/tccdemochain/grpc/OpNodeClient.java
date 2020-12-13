package com.github.zibuyu28.tccdemochain.grpc;

import com.github.zibuyu28.node.service.Action;
import com.github.zibuyu28.node.service.OpNodeResponse;
import com.github.zibuyu28.node.service.OpNodeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.dromara.hmily.grpc.client.GrpcHmilyClient;
import org.dromara.hmily.grpc.filter.GrpcHmilyTransactionFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OpNodeClient {

    private OpNodeServiceGrpc.OpNodeServiceBlockingStub opNodeServiceBlockingStub;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9082)
                .intercept(new GrpcHmilyTransactionFilter()).usePlaintext().build();
        opNodeServiceBlockingStub = OpNodeServiceGrpc.newBlockingStub(managedChannel);
    }

    public boolean updateNodeConfig(int nodeID, String config) {
        Action action = Action.newBuilder()
                .setNodeID(nodeID)
                .setAction("updateConfig")
                .setParam(config).build();

        OpNodeResponse response = GrpcHmilyClient.syncInvoke(opNodeServiceBlockingStub, "doAction", action, OpNodeResponse.class);
        if(response != null) {
            return response.getCode() == 200;
        } else {
            return false;
        }
    }
}
