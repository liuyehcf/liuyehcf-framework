package com.github.liuyehcf.framework.rpc.maple;

import com.github.liuyehcf.framework.rpc.maple.register.ConfigClient;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;
import com.github.liuyehcf.framework.rpc.maple.util.Assert;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chenlu
 * @date 2019/3/15
 */
@Data
public class MapleSpringConsumerBean implements FactoryBean, InitializingBean {

    private final AtomicBoolean init = new AtomicBoolean(false);
    private ConfigClient configClient;
    private ServiceMeta serviceMeta;
    private volatile Object target;
    private Class<?> objectType;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public Object getObject() {
        assertInit();
        return target;
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    private void init() {
        if (isAlreadyInit()) {
            return;
        }

        checkParams();

        createProxy();
    }

    private void checkParams() {
        Assert.assertNotNull(configClient, "configClient required");
        Assert.assertNotNull(serviceMeta, "serviceMeta required");
    }

    private boolean isAlreadyInit() {
        return !init.compareAndSet(false, true);
    }

    private void assertInit() {
        Assert.assertTrue(init.get(), "MapleSpringConsumerBean hasn't init");
    }

    private void createProxy() {
        this.target = Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class<?>[]{objectType, GenericService.class},
                new MapleConsumerInvocationHandler(configClient, serviceMeta));
    }
}
