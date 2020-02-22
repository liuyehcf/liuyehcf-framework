package com.github.liuyehcf.framework.rpc.maple.test.provider.service;


import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.common.tools.bean.JavaBeanInitializer;
import com.github.liuyehcf.framework.rpc.maple.MapleProvider;
import com.github.liuyehcf.framework.rpc.maple.netty.provider.ProviderAddress;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceAddress;
import com.github.liuyehcf.framework.rpc.maple.test.common.BizRequest;
import com.github.liuyehcf.framework.rpc.maple.test.common.BizResponse;
import com.github.liuyehcf.framework.rpc.maple.test.common.GreetService;
import com.github.liuyehcf.framework.rpc.maple.test.common.MapleResult;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
@MapleProvider(serviceInterface = GreetService.class, serviceGroup = "${maple.provider.group}", serviceVersion = "${maple.provider.version}")
public class GreetServiceImpl implements GreetService {

    @Override
    public String sayHello(String name) {
        ServiceAddress address = ProviderAddress.getServiceAddress();
        return String.format("Hello, %s. This is '%s:%d'. timestamp=%d", name, address.getHost(), address.getPort(), System.currentTimeMillis());
    }

    @Override
    public MapleResult<BizResponse> request(BizRequest request) {
        ServiceAddress address = ProviderAddress.getServiceAddress();

        System.err.println(String.format("address=%s, request=%s", JSON.toJSONString(address), JSON.toJSONString(request)));

        MapleResult<BizResponse> response = JavaBeanInitializer.createJavaBean(new JavaBeanInitializer.TypeReference<MapleResult<BizResponse>>() {
        });

        System.err.println(String.format("response=%s", JSON.toJSONString(response)));

        return response;
    }

    @Override
    public void throwException() {
        throw new RuntimeException("expected runtime exception");
    }
}
