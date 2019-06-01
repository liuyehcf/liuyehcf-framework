package com.github.liuyehcf.framework.rpc.maple.register;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author chenlu
 * @date 2019/3/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultServiceInstance implements ServiceInstance {

    private ServiceAddress serviceAddress;

    private ServiceMeta serviceMeta;

    public static ServiceInstance parse(Map<String, Object> map) {
        ServiceAddress serviceAddress = null;
        ServiceMeta serviceMeta = null;

        if (map.containsKey("serviceAddress")) {
            serviceAddress = JSON.parseObject(JSON.toJSONString(map.get("serviceAddress")), DefaultServiceAddress.class);
        }

        if (map.containsKey("serviceMeta")) {
            serviceMeta = JSON.parseObject(JSON.toJSONString(map.get("serviceMeta")), DefaultServiceMeta.class);
        }

        return new DefaultServiceInstance(serviceAddress, serviceMeta);
    }

    @Override
    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    @Override
    public ServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
