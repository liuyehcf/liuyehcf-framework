package com.github.liuyehcf.framework.rpc.ares.spring;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import sun.net.ConnectionResetException;

import java.io.IOException;
import java.util.List;

@ConfigurationProperties(prefix = "ares.http.config")
@Data
public class AresAutoConfiguration {

    private int maxTotal = 200;
    private int defaultMaxPerRoute = 200;
    private int retryTimes = 3;
    private int socketTimeout = 2000;
    private int connectTimeout = 2000;
    private int connectionRequestTimeout = 2000;

    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setRetryHandler((IOException exception, int executionCount, HttpContext context) -> {
                    if (executionCount > retryTimes) {
                        return false;
                    }
                    return exception instanceof NoHttpResponseException
                            || exception instanceof ConnectionResetException;
                })
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public Gson gson(@Autowired List<AresJsonSerializer> serializers,
                     @Autowired List<AresJsonDeserializer> deserializers) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (CollectionUtils.isNotEmpty(serializers)) {
            for (AresJsonSerializer serializer : serializers) {
                gsonBuilder.registerTypeAdapter(serializer.getType(), serializer.getSerializer());
            }
        }

        if (CollectionUtils.isNotEmpty(deserializers)) {
            for (AresJsonDeserializer deserializer : deserializers) {
                gsonBuilder.registerTypeAdapter(deserializer.getType(), deserializer.getDeserializer());
            }
        }

        return gsonBuilder.create();
    }

    @Bean
    public BeanFactoryPostProcessor aresConsumerSpringPostProcessor() {
        return new AresConsumerSpringPostProcessor();
    }
}
