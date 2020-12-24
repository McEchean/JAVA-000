package io.kimmking.rpcfx.client.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

public class JSONCodec implements Codec<String,RpcfxRequest,RpcfxResponse> {
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }
    @Override
    public String encode(RpcfxRequest o) {
        return JSON.toJSONString(o);
    }

    @Override
    public RpcfxResponse decode(String o) {
        return JSON.parseObject(o, RpcfxResponse.class);
    }
}
