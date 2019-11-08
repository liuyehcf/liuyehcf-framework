package com.github.liuyehcf.framework.rpc.http.spring;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@Data
public class AresSpringConsumerBean implements FactoryBean, InitializingBean {

    private Object target;
    private Class<?> objectType;
    private String schema;
    private String host;
    private int port;

    @Override
    public Object getObject() {
        return target;
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    @Override
    public void afterPropertiesSet() {
        createProxy();
    }

    private void createProxy() {
        this.target = Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class<?>[]{objectType},
                new AresConsumerInvocationHandler(schema, host, port));
    }
}
