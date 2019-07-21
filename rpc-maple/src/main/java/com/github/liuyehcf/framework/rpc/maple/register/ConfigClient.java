package com.github.liuyehcf.framework.rpc.maple.register;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public interface ConfigClient {

    /**
     * post provider's address and meta information to config center
     *
     * @param serviceMeta    service metadata
     * @param serviceAddress service address
     */
    void registerService(final ServiceMeta serviceMeta, final ServiceAddress serviceAddress);

    /**
     * fetch all service instance of specified service metadata
     *
     * @param serviceMeta service metadata
     * @return list of service address
     */
    List<ServiceInstance> fetchAllServiceAddresses(final ServiceMeta serviceMeta);

    /**
     * fetch any matched service instance of specified service metadata
     *
     * @param serviceMeta service metadata
     * @return service address
     */
    ServiceInstance fetchAnyServiceAddress(final ServiceMeta serviceMeta);

}
