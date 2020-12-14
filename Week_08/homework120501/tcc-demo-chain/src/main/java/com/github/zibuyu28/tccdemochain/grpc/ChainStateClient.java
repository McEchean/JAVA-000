package com.github.zibuyu28.tccdemochain.grpc;

import com.github.zibuyu28.chain.service.ChainState;
import com.github.zibuyu28.chain.service.ChainStateResponse;
import com.github.zibuyu28.chain.service.ChainStatusServiceGrpc;
import com.github.zibuyu28.node.service.Action;
import com.github.zibuyu28.node.service.OpNodeResponse;
import com.github.zibuyu28.node.service.OpNodeServiceGrpc;
import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.dromara.hmily.grpc.client.GrpcHmilyClient;
import org.dromara.hmily.grpc.filter.GrpcHmilyTransactionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ChainStateClient {
    private ChainStatusServiceGrpc.ChainStatusServiceBlockingStub chainStatusServiceBlockingStub;

    @Autowired
    private GrpcHmilyClient grpcHmilyClient;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9081)
                .intercept(new GrpcHmilyTransactionFilter()).usePlaintext().build();
        chainStatusServiceBlockingStub = ChainStatusServiceGrpc.newBlockingStub(managedChannel);
    }

    public boolean updateChainState(int chainID, int state, String message) {
        ChainState chainState = ChainState.newBuilder()
                .setChainID(chainID)
                .setState(state)
                .setMessage(message).build();

        ChainStateResponse response = grpcHmilyClient.syncInvoke(chainStatusServiceBlockingStub, "updateStatus", chainState, ChainStateResponse.class);
        if(response != null) {
            return response.getCode() == 200;
        } else {
            return false;
        }
    }
}
