package com.github.liuyehcf.framework.rpc.maple.test.provider;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.maple.MapleConst;
import com.github.liuyehcf.framework.rpc.maple.MapleSpringProviderBean;
import com.github.liuyehcf.framework.rpc.maple.netty.provider.ProviderAddress;
import com.github.liuyehcf.framework.rpc.maple.register.DefaultServiceMeta;
import com.github.liuyehcf.framework.rpc.maple.register.ZookeeperConfigClient;
import com.github.liuyehcf.framework.rpc.maple.test.common.GreetService;
import com.github.liuyehcf.framework.rpc.maple.test.provider.service.GreetServiceImpl;

import java.util.concurrent.TimeUnit;

/**
 * @author chenlu
 * @date 2019/3/23
 */
public class MainProvider {
    public static void main(String[] args) throws Exception {
        MapleSpringProviderBean providerBean = new MapleSpringProviderBean();
        providerBean.setServiceMeta(
                DefaultServiceMeta.builder()
                        .serviceInterface(GreetService.class.getName())
                        .serviceGroup(MapleConst.DEFAULT_GROUP)
                        .serviceVersion(MapleConst.DEFAULT_VERSION)
                        .build()
        );

        ZookeeperConfigClient configClient = new ZookeeperConfigClient();
        configClient.setHost("localhost");
        configClient.setPort(2181);
        configClient.afterPropertiesSet();

        providerBean.setConfigClient(configClient);
        providerBean.setTarget(new GreetServiceImpl());

        providerBean.afterPropertiesSet();

        System.err.println(JSON.toJSONString(ProviderAddress.getServiceAddress()));

        TimeUnit.DAYS.sleep(1);
    }
}
