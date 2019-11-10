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
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author hechenfeng
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

                String content = serialize(param.target, param.serializeType);
                if (content != null) {
                    uriBuilder.addParameter(entry.getKey(), content);
                }
            }
        }

        if (params.requestBody != null) {
            String content = serialize(params.requestBody.target, params.requestBody.serializeType);

            if (content != null) {
                builder.setEntity(new ByteArrayEntity(content.getBytes()));
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

            return deserialize(entity, responseDeserializeType, method);

        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
    }

    private String serialize(Object value, SerializeType serializeType) {
        if (value == null) {
            return null;
        }

        Class<?> type = value.getClass();

        if (ClassUtils.isPrimitiveOrWrapper(type)) {
            return value.toString();
        } else if (String.class.equals(type)) {
            return (String) value;
        } else if (BigInteger.class.equals(type)) {
            return value.toString();
        } else if (BigDecimal.class.equals(type)) {
            return value.toString();
        } else {
            switch (serializeType) {
                case json:
                    try {
                        return JSON.toJSONString(value);
                    } catch (Exception e) {
                        throw new AresException("failed to serialize object to json string: " + type.getName());
                    }
                default:
                    throw new UnsupportedOperationException(String.format("unexpected serialize type '%s'", serializeType));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object deserialize(String entity, SerializeType serializeType, Method method) {
        Class<?> type = method.getReturnType();
        if (void.class.equals(type) || Void.class.equals(type)) {
            return null;
        } else if (ClassUtils.isPrimitiveOrWrapper(type)) {
            boolean isPrimitive = type.isPrimitive();
            boolean isEmpty = StringUtils.EMPTY.equals(entity);
            if (boolean.class.equals(type)
                    || Boolean.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return false;
                    } else {
                        return null;
                    }
                }
                return Boolean.valueOf(entity);
            } else if (byte.class.equals(type)
                    || Byte.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return (byte) 0;
                    } else {
                        return null;
                    }
                }
                return Byte.valueOf(entity);
            } else if (short.class.equals(type)
                    || Short.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return (short) 0;
                    } else {
                        return null;
                    }
                }
                return Short.valueOf(entity);
            } else if (int.class.equals(type)
                    || Integer.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return 0;
                    } else {
                        return null;
                    }
                }
                return Integer.valueOf(entity);
            } else if (long.class.equals(type)
                    || Long.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return 0L;
                    } else {
                        return null;
                    }
                }
                return Long.valueOf(entity);
            } else if (float.class.equals(type)
                    || Float.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return 0f;
                    } else {
                        return null;
                    }
                }
                return Float.valueOf(entity);
            } else if (double.class.equals(type)
                    || Double.class.equals(type)) {
                if (isEmpty) {
                    if (isPrimitive) {
                        return 0d;
                    } else {
                        return null;
                    }
                }
                return Double.valueOf(entity);
            } else {
                throw new UnsupportedOperationException("unsupported primitive(or wrapper) type");
            }
        } else {
            boolean isEmpty = StringUtils.EMPTY.equals(entity);

            if (isEmpty) {
                return null;
            } else if (String.class.equals(type)) {
                return entity;
            } else if (BigInteger.class.equals(type)) {
                return new BigInteger(entity);
            } else if (BigDecimal.class.equals(type)) {
                return new BigDecimal(entity);
            } else {
                switch (serializeType) {
                    case json:
                        try {
                            return JSON.parseObject(entity, method.getGenericReturnType());
                        } catch (Exception e) {
                            throw new AresException("failed to parse json object from: " + entity);
                        }
                    default:
                        throw new UnsupportedOperationException(String.format("unexpected serialize type '%s'", serializeType));
                }
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
