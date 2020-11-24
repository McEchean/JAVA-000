package com.github.zibuyu28.mq;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse implements Serializable {

//    private FullHttpResponse resp;

    private String ctxAddr;

    private byte[] responseBody;
}
