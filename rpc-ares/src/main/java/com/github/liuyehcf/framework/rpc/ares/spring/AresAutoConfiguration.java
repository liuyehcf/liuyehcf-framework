package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;
import com.github.liuyehcf.framework.rpc.ares.ObjectToStringCodes;
import com.github.liuyehcf.framework.rpc.ares.codes.*;
import lombok.Data;
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
import java.util.Collections;
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
    public BeanFactoryPostProcessor aresConsumerSpringPostProcessor(@Autowired List<ObjectToStringCodes<?>> stringCodes,
                                                                    @Autowired List<ObjectToBytesCodes<?>> byteCodes) {
        Collections.sort(stringCodes);
        Collections.sort(byteCodes);
        return new AresConsumerSpringPostProcessor(stringCodes, byteCodes);
    }

    @Bean(name = "aresBytesToBytesCodes")
    public BytesToBytesCodes bytesToByteCodes() {
        return new BytesToBytesCodes();
    }

    @Bean(name = "aresBooleanToBytesCodes")
    public BooleanToBytesCodes booleanToBytesCodes() {
        return new BooleanToBytesCodes();
    }

    @Bean(name = "aresByteToBytesCodes")
    public ByteToBytesCodes byteToByteCodes() {
        return new ByteToBytesCodes();
    }

    @Bean(name = "aresShortToBytesCodes")
    public ShortToBytesCodes shortToByteCodes() {
        return new ShortToBytesCodes();
    }

    @Bean(name = "aresIntToBytesCodes")
    public IntToBytesCodes intToByteCodes() {
        return new IntToBytesCodes();
    }

    @Bean(name = "aresLongToBytesCodes")
    public LongToBytesCodes longToByteCodes() {
        return new LongToBytesCodes();
    }

    @Bean(name = "aresFloatToBytesCodes")
    public FloatToBytesCodes floatToByteCodes() {
        return new FloatToBytesCodes();
    }

    @Bean(name = "aresDoubleToBytesCodes")
    public DoubleToBytesCodes doubleToByteCodes() {
        return new DoubleToBytesCodes();
    }

    @Bean(name = "aresVoidToBytesCodes")
    public VoidToBytesCodes voidToBytesCodes() {
        return new VoidToBytesCodes();
    }

    @Bean(name = "aresStringToBytesCodes")
    public StringToBytesCodes stringToBytesCodes() {
        return new StringToBytesCodes();
    }

    @Bean(name = "aresMapToBytesCodes")
    public MapToBytesCodes mapToBytesCodes() {
        return new MapToBytesCodes();
    }

    @Bean(name = "aresPojoToBytesCodes")
    public PojoToBytesCodes pojoToBytesCodes() {
        return new PojoToBytesCodes();
    }

    @Bean(name = "aresBigIntegerToBytesCodes")
    public BigIntegerToBytesCodes bigIntegerToBytesCodes() {
        return new BigIntegerToBytesCodes();
    }

    @Bean(name = "aresBigDecimalToBytesCodes")
    public BigDecimalToBytesCodes bigDecimalToBytesCodes() {
        return new BigDecimalToBytesCodes();
    }
}
