package com.github.liuyehcf.framework.rpc.maple.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author chenlu
 * @date 2019/3/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DefaultServiceAddress implements ServiceAddress {

    private String host;

    private int port;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
