package io.kimmking.rpcfx.client.invoke.nettyclient.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostAndPortConfig {

    private String host;

    private Integer port;


}
