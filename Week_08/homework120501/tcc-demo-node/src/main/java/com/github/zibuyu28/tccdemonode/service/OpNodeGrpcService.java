package com.github.zibuyu28.tccdemonode.service;

import com.github.zibuyu28.chain.service.ChainStateResponse;
import com.github.zibuyu28.node.service.Action;
import com.github.zibuyu28.node.service.OpNodeResponse;
import com.github.zibuyu28.node.service.OpNodeServiceGrpc;
import com.github.zibuyu28.tccdemonode.dao.Node;
import io.grpc.stub.StreamObserver;
import org.dromara.hmily.grpc.filter.GrpcHmilyServerFilter;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.zibuyu28.tccdemonode.service.Op.UPDATE_CONFIG;

@GRpcService(interceptors = {GrpcHmilyServerFilter.class})
public class OpNodeGrpcService extends OpNodeServiceGrpc.OpNodeServiceImplBase {
    @Autowired
    private OpNodeService opNodeService;

    @Override
    public void doAction(Action request, StreamObserver<OpNodeResponse> responseObserver) {
        String action = request.getAction();
        if (UPDATE_CONFIG.getName().equals(action)) {
            Node node = new Node(request.getNodeID(), request.getParam());
            OpNodeResponse opNodeResponse;
            if (opNodeService.UpdateConfig(node)) {
                opNodeResponse = OpNodeResponse.newBuilder()
                        .setCode(200)
                        .setData("success")
                        .setErr("").build();

            } else {
                opNodeResponse = OpNodeResponse.newBuilder()
                        .setCode(100)
                        .setData("failed")
                        .setErr("update node config err").build();
            }
            responseObserver.onNext(opNodeResponse);
            responseObserver.onCompleted();
        }
    }
}
