package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;
import com.github.liuyehcf.framework.rpc.ares.ObjectToStringCodes;
import lombok.Data;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Data
public class AresSpringConsumerBean implements FactoryBean<Object>, InitializingBean {

    @Resource
    private HttpClient httpClient;
    @Resource
    private RequestConfig requestConfig;

    private List<ObjectToStringCodes<?>> stringCodes;
    private List<ObjectToBytesCodes<?>> byteCodes;
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
                new AresConsumerInvocationHandler(httpClient, requestConfig, stringCodes, byteCodes, schema, host, port));
    }
}
