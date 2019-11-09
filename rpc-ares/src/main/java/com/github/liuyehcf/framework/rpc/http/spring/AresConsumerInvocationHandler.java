package com.github.liuyehcf.framework.rpc.http.spring;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rpc.http.AresException;
import com.github.liuyehcf.framework.rpc.http.AresMethod;
import com.github.liuyehcf.framework.rpc.http.AresRequestBody;
import com.github.liuyehcf.framework.rpc.http.AresRequestParam;
import com.github.liuyehcf.framework.rpc.http.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.http.constant.SerializeType;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * todo 如果接收方的方法返回值是void，但实际并不是
 *
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
class AresConsumerInvocationHandler implements InvocationHandler {

    private final HttpClient httpClient;
    private final RequestConfig requestConfig;

    private final String schema;
    private final String host;
    private final int port;

    AresConsumerInvocationHandler(HttpClient httpClient, RequestConfig requestConfig, String schema, String host, int port) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
        this.schema = schema;
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AresMethod aresMethodAnnotation = AnnotationUtils.getAnnotation(method, AresMethod.class);
        Assert.assertNotNull(aresMethodAnnotation, "http consumer missing AresMethod");

        String path = aresMethodAnnotation.path();
        HttpMethod httpMethod = aresMethodAnnotation.method();
        SerializeType responseDeserializeType = aresMethodAnnotation.responseDeserializeType();

        Params params = parseQueryParams(method.getParameters(), args);

        HttpRequestBase httpRequest = buildRequest(path, httpMethod, params);

        return doInvoke(httpRequest, method, responseDeserializeType);
    }

    private Params parseQueryParams(Parameter[] parameters, Object[] args) {
        Map<String, Param> queryParams = Maps.newHashMap();

        boolean hasRequestBody = false;
        Param requestBody = null;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            AresRequestParam aresRequestParam = AnnotationUtils.getAnnotation(parameter, AresRequestParam.class);
            AresRequestBody aresRequestBody = AnnotationUtils.getAnnotation(parameter, AresRequestBody.class);

            if (aresRequestParam != null &&
                    aresRequestBody != null) {
                throw new AresException("parameter contains both '@AresRequestParam' and '@AresRequestBody'");
            } else if (aresRequestParam != null) {
                if (queryParams.containsKey(aresRequestParam.name())) {
                    throw new AresException(String.format("duplicate query parameter '%s'", aresRequestParam.name()));
                }
                queryParams.put(aresRequestParam.name(), new Param(args[i], aresRequestParam.serializeType()));
            } else if (aresRequestBody != null) {
                Assert.assertFalse(hasRequestBody, "more than one '@AresRequestBody'");
                hasRequestBody = true;
                requestBody = new Param(args[i], aresRequestBody.serializeType());
            } else {
                throw new AresException("parameter missing '@AresRequestParam' or '@AresRequestBody'");
            }
        }

        return new Params(queryParams, requestBody);
    }

    private HttpRequestBase buildRequest(String path, HttpMethod httpMethod, Params params) throws Exception {
        RequestBuilder builder = RequestBuilder.create(httpMethod.name());

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(schema);
        uriBuilder.setHost(host);
        uriBuilder.setPort(port);
        uriBuilder.setPath(path);
        if (MapUtils.isNotEmpty(params.queryParams)) {
            for (Map.Entry<String, Param> entry : params.queryParams.entrySet()) {
                Param param = entry.getValue();
                switch (param.serializeType) {
                    case string:
                        if (param.target != null) {
                            uriBuilder.addParameter(entry.getKey(), param.target.toString());
                        }
                        break;
                    case json:
                        if (param.target != null) {
                            uriBuilder.addParameter(entry.getKey(), JSON.toJSONString(param.target));
                        }
                        break;
                    default:
                        throw new UnsupportedOperationException(String.format("unexpected query parameter serialize type '%s'", param.serializeType));
                }
            }
        }

        if (params.requestBody != null) {
            switch (params.requestBody.serializeType) {
                case string:
                    if (params.requestBody.target != null) {
                        builder.setEntity(new ByteArrayEntity(params.requestBody.target.toString().getBytes()));
                    }
                    break;
                case json:
                    if (params.requestBody.target != null) {
                        builder.setEntity(new ByteArrayEntity(JSON.toJSONBytes(params.requestBody.target)));
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("unexpected request body serialize type '%s'", params.requestBody.serializeType));
            }
        }

        builder.setUri(uriBuilder.build().toASCIIString());

        return (HttpRequestBase) builder.build();
    }

    private Object doInvoke(HttpRequestBase httpRequest, Method method, SerializeType responseDeserializeType) throws Exception {
        httpRequest.setConfig(requestConfig);

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            String entity = new String(EntityUtils.toByteArray(response.getEntity()));

            if (HttpStatus.SC_OK != statusCode) {
                throw new AresException(String.format("http request failed, code=%d; message=%s",
                        statusCode,
                        entity));
            }

            if (void.class.equals(method.getReturnType()) || Void.class.equals(method.getReturnType())) {
                return null;
            } else if (ClassUtils.isPrimitiveOrWrapper(method.getReturnType())) {
                if (boolean.class.equals(method.getReturnType())
                        || Boolean.class.equals(method.getReturnType())) {
                    return Boolean.valueOf(entity);
                } else if (byte.class.equals(method.getReturnType())
                        || Byte.class.equals(method.getReturnType())) {
                    return Byte.valueOf(entity);
                } else if (short.class.equals(method.getReturnType())
                        || Short.class.equals(method.getReturnType())) {
                    return Short.valueOf(entity);
                } else if (int.class.equals(method.getReturnType())
                        || Integer.class.equals(method.getReturnType())) {
                    return Integer.valueOf(entity);
                } else if (long.class.equals(method.getReturnType())
                        || Long.class.equals(method.getReturnType())) {
                    return Long.valueOf(entity);
                } else if (float.class.equals(method.getReturnType())
                        || Float.class.equals(method.getReturnType())) {
                    return Float.valueOf(entity);
                } else if (double.class.equals(method.getReturnType())
                        || Double.class.equals(method.getReturnType())) {
                    return Double.valueOf(entity);
                }
            }

            switch (responseDeserializeType) {
                case string:
                    return entity;
                case json:
                    try {
                        return JSON.parseObject(entity, method.getGenericReturnType());
                    } catch (Exception e) {
                        throw new AresException("failed to parse json object from: " + entity);
                    }
                default:
                    throw new UnsupportedOperationException(String.format("unexpected response body deserialize type '%s'", responseDeserializeType));
            }
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
    }

    private static final class Params {
        private final Map<String, Param> queryParams;
        private final Param requestBody;

        private Params(Map<String, Param> queryParams, Param requestBody) {
            this.queryParams = queryParams;
            this.requestBody = requestBody;
        }
    }

    private static final class Param {
        private final Object target;
        private final SerializeType serializeType;

        private Param(Object target, SerializeType serializeType) {
            this.target = target;
            this.serializeType = serializeType;
        }
    }
}
