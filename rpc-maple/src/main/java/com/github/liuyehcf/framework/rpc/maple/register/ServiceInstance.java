package com.github.liuyehcf.framework.rpc.maple.register;

/**
 * @author chenlu
 * @date 2019/3/25
 */
public interface ServiceInstance {

    /**
     * service address
     */
    ServiceAddress getServiceAddress();

    /**
     * service meta
     */
    ServiceMeta getServiceMeta();

}
