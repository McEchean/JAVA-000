package com.github.zibuyu28.mq;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest implements Serializable {
    private String reqURI;

    private HashMap<String, String> headers;

    private String ctxAddr;
}
