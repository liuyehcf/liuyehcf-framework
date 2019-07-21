package com.github.liuyehcf.framework.rpc.maple.register;

/**
 * @author hechenfeng
 * @date 2019/3/21
 */
public interface ServiceAddress {

    /**
     * host of provider's address
     */
    String getHost();

    /**
     * port of provider's address
     */
    int getPort();
}
