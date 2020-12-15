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

import io.kimmking.rpcfx.client.utils.ModuleUtils;
import io.netty.handler.codec.http.HttpMethod;
import lombok.Data;

/**
 * http + json
 */
@Data
public class NettyHttpRequest {
    /**
     * 默认post
     */
    HttpMethod httpMethod = HttpMethod.POST;

    private Object body;
    /**
     * 包含了全部信息，如：
     * http://192.168.19.102:8080/BOL_WebService/ElectronicFootRingAlarm.do
     */
    private String fullUrl;

    /**
     * 不包含url前面的协议、ip、端口，如：
     * /BOL_WebService/ElectronicFootRingAlarm.do
     */
    private String uri;

    String port;



    /**
     *
     * @param fullUrl 全路径，
     *                如：http://192.168.19.102:8080/BOL_WebService/ElectronicFootRingAlarm.do
     * @param body  请求体，后续会序列化为json
     */
    public NettyHttpRequest(String fullUrl, Object body) {
        this.body = body;
        this.fullUrl = fullUrl;
        port = ModuleUtils.extractPort(fullUrl);

        int index = fullUrl.indexOf(port);
        this.uri = fullUrl.substring(index + port.length());
    }



}
