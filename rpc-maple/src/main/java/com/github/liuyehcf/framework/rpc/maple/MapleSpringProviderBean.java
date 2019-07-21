package com.github.liuyehcf.framework.rpc.maple;

import com.github.liuyehcf.framework.rpc.maple.netty.provider.ProviderAddress;
import com.github.liuyehcf.framework.rpc.maple.netty.provider.ProviderCache;
import com.github.liuyehcf.framework.rpc.maple.netty.provider.ProviderLoop;
import com.github.liuyehcf.framework.rpc.maple.register.ConfigClient;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;
import com.github.liuyehcf.framework.rpc.maple.util.Assert;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hechenfeng
 * @date 2019/3/15
 */
@Data
public class MapleSpringProviderBean implements InitializingBean {

    private final AtomicBoolean init = new AtomicBoolean(false);
    private ConfigClient configClient;
    private ServiceMeta serviceMeta;
    private Object target;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init() throws Exception {
        if (isAlreadyInit()) {
            return;
        }

        checkParams();

        bootEventLoop();

        registerService();
    }

    private boolean isAlreadyInit() {
        return !init.compareAndSet(false, true);
    }

    private void checkParams() {
        Assert.assertNotNull(configClient, "configClient required");
        Assert.assertNotNull(serviceMeta, "serviceMeta required");
        Assert.assertNotNull(target, "target required");
    }

    private void bootEventLoop() throws Exception {
        ProviderLoop.boot();
    }

    private void registerService() {
        ProviderCache.addProviderTargetBean(serviceMeta, target);

        configClient.registerService(serviceMeta, ProviderAddress.getServiceAddress());
    }
}
