package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.rpc.ares.ParamsConverter;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.ResponseHandler;
import com.github.liuyehcf.framework.rpc.ares.converters.params.ObjectParamsConverter;
import com.github.liuyehcf.framework.rpc.ares.converters.reqbody.*;
import com.github.liuyehcf.framework.rpc.ares.converters.resbody.*;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    private boolean enableDefaultParamsConverters = true;
    private boolean enableDefaultRequestBodyConverters = true;
    private boolean enableDefaultResponseBodyConverters = true;

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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BeanFactoryPostProcessor aresConsumerSpringPostProcessor(@Autowired List<ParamsConverter<?>> paramsConverters,
                                                                    @Autowired List<RequestBodyConverter<?>> requestBodyConverters,
                                                                    @Autowired List<ResponseBodyConverter<?>> responseBodyConverters,
                                                                    @Autowired List<ResponseHandler> responseHandlers) {
        Collections.sort((List<? extends Comparable>) paramsConverters);
        Collections.sort((List<? extends Comparable>) requestBodyConverters);
        Collections.sort((List<? extends Comparable>) responseBodyConverters);
        return new AresConsumerSpringPostProcessor(paramsConverters, requestBodyConverters, responseBodyConverters, responseHandlers);
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultParamsConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresObjectParamsConverter")
    public ObjectParamsConverter objectParamsConverter() {
        return new ObjectParamsConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresVoidRequestBodyConverter")
    public VoidRequestBodyConverter voidRequestBodyConverter() {
        return new VoidRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBooleanRequestBodyConverter")
    public BooleanRequestBodyConverter booleanRequestBodyConverter() {
        return new BooleanRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresByteRequestBodyConverter")
    public ByteRequestBodyConverter byteRequestBodyConverter() {
        return new ByteRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresShortRequestBodyConverter")
    public ShortRequestBodyConverter shortRequestBodyConverter() {
        return new ShortRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresIntegerRequestBodyConverter")
    public IntegerRequestBodyConverter integerRequestBodyConverter() {
        return new IntegerRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresLongRequestBodyConverter")
    public LongRequestBodyConverter longRequestBodyConverter() {
        return new LongRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresFloatRequestBodyConverter")
    public FloatRequestBodyConverter floatRequestBodyConverter() {
        return new FloatRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresDoubleRequestBodyConverter")
    public DoubleRequestBodyConverter doubleRequestBodyConverter() {
        return new DoubleRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBytesRequestBodyConverter")
    public BytesRequestBodyConverter bytesRequestBodyConverter() {
        return new BytesRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresStringRequestBodyConverter")
    public StringRequestBodyConverter stringRequestBodyConverter() {
        return new StringRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBigIntegerRequestBodyConverter")
    public BigIntegerRequestBodyConverter bigIntegerRequestBodyConverter() {
        return new BigIntegerRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBigDecimalRequestBodyConverter")
    public BigDecimalRequestBodyConverter bigDecimalRequestBodyConverter() {
        return new BigDecimalRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresMapRequestBodyConverter")
    public MapRequestBodyConverter mapRequestBodyConverter() {
        return new MapRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultRequestBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresPojoRequestBodyConverter")
    public PojoRequestBodyConverter pojoRequestBodyConverter() {
        return new PojoRequestBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresVoidResponseBodyConverter")
    public VoidResponseBodyConverter voidResponseBodyConverter() {
        return new VoidResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBooleanResponseBodyConverter")
    public BooleanResponseBodyConverter booleanResponseBodyConverter() {
        return new BooleanResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresByteResponseBodyConverter")
    public ByteResponseBodyConverter byteResponseBodyConverter() {
        return new ByteResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresShortResponseBodyConverter")
    public ShortResponseBodyConverter shortResponseBodyConverter() {
        return new ShortResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresIntegerResponseBodyConverter")
    public IntegerResponseBodyConverter integerResponseBodyConverter() {
        return new IntegerResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresLongResponseBodyConverter")
    public LongResponseBodyConverter longResponseBodyConverter() {
        return new LongResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresFloatResponseBodyConverter")
    public FloatResponseBodyConverter floatResponseBodyConverter() {
        return new FloatResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresDoubleResponseBodyConverter")
    public DoubleResponseBodyConverter doubleResponseBodyConverter() {
        return new DoubleResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBytesResponseBodyConverter")
    public BytesResponseBodyConverter bytesResponseBodyConverter() {
        return new BytesResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresStringResponseBodyConverter")
    public StringResponseBodyConverter stringResponseBodyConverter() {
        return new StringResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBigIntegerResponseBodyConverter")
    public BigIntegerResponseBodyConverter bigIntegerResponseBodyConverter() {
        return new BigIntegerResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresBigDecimalResponseBodyConverter")
    public BigDecimalResponseBodyConverter bigDecimalResponseBodyConverter() {
        return new BigDecimalResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresMapResponseBodyConverter")
    public MapResponseBodyConverter mapResponseBodyConverter() {
        return new MapResponseBodyConverter();
    }

    @ConditionalOnProperty(prefix = "ares.http.config", name = "enableDefaultResponseBodyConverters", havingValue = "false", matchIfMissing = true)
    @Bean(name = "aresPojoResponseBodyConverter")
    public PojoResponseBodyConverter pojoResponseBodyConverter() {
        return new PojoResponseBodyConverter();
    }
}
