package com.github.zibuyu28.tccdemochain.service;

import com.github.zibuyu28.chain.service.ChainState;
import com.github.zibuyu28.chain.service.ChainStateResponse;
import com.github.zibuyu28.chain.service.ChainStatusServiceGrpc;
import com.github.zibuyu28.tccdemochain.dao.ChainStatus;
import io.grpc.stub.StreamObserver;
import org.dromara.hmily.grpc.filter.GrpcHmilyServerFilter;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService(interceptors = {GrpcHmilyServerFilter.class})
public class ChainStateGrpcService extends ChainStatusServiceGrpc.ChainStatusServiceImplBase {

    @Autowired
    private ChainStateServcie chainStateServcie;

    @Override
    public void updateStatus(ChainState request, StreamObserver<ChainStateResponse> responseObserver) {
        ChainStatus chainStatus = ChainStatus.builder()
                .id(request.getChainID())
                .state(request.getState())
                .message(request.getMessage()).build();

        ChainStateResponse chainStateResponse = null;
        if (chainStateServcie.update(chainStatus)) {
            chainStateResponse = ChainStateResponse.newBuilder()
                    .setCode(200)
                    .setData("success")
                    .setErr("").build();

        } else {
            chainStateResponse = ChainStateResponse.newBuilder()
                    .setCode(100)
                    .setData("failed")
                    .setErr("update chain state err").build();
        }

        responseObserver.onNext(chainStateResponse);
        responseObserver.onCompleted();

    }
}
