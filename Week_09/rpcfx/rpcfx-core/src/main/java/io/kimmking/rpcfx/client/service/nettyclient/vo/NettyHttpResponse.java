/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kimmking.rpcfx.client.service.nettyclient.vo;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * http + json
 */
@Data
@AllArgsConstructor
public class NettyHttpResponse {
    /**
     * 响应码，比如200
     */
    private HttpResponseStatus httpResponseStatus;

    /**
     * 消息体,utf-8解码
     */
    private String body;



    public static NettyHttpResponse successResponse(String body) {
        return new NettyHttpResponse(HttpResponseStatus.OK, body);
    }
}
