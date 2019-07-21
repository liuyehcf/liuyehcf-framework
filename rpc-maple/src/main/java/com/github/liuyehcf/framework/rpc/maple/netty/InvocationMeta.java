package com.github.liuyehcf.framework.rpc.maple.netty;

import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public interface InvocationMeta extends Serializable {

    /**
     * service meta
     */
    ServiceMeta getServiceMeta();

    /**
     * method name
     */
    String getMethodName();

    /**
     * parameter type
     */
    String[] getParameterTypes();
}
