package com.github.liuyehcf.framework.rpc.maple.register;

import java.io.Serializable;

/**
 * @author chenlu
 * @date 2019/3/15
 */
public interface ServiceMeta extends Serializable {
    /**
     * get service interface
     */
    String getServiceInterface();

    /**
     * get service group
     */
    String getServiceGroup();

    /**
     * get service version
     */
    String getServiceVersion();

    /**
     * client timeout
     */
    long getClientTimeout();

    /**
     * get serialize type
     */
    byte getSerializeType();
}
