package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.rpc.ares.ParamsConverter;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.ResponseHandler;
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
public class AresSpringConsumerBean implements FactoryBean<Object>, InitializingBean {

    @Resource
    private HttpClient httpClient;
    @Resource
    private RequestConfig requestConfig;

    private List<ParamsConverter<?>> paramsConverters;
    private List<RequestBodyConverter<?>> requestBodyConverters;
    private List<ResponseBodyConverter<?>> responseBodyConverters;
    private List<ResponseHandler> responseHandlers;
    private Object target;
    private Class<?> objectType;
    private String schema;
    private String host;
    private int port;

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

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
                new AresConsumerInvocationHandler(httpClient, requestConfig, paramsConverters, requestBodyConverters, responseBodyConverters, responseHandlers, schema, host, port));
    }
}
