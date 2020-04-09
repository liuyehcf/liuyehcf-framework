package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.rpc.ares.*;
import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.ares.util.AresContext;
import com.github.liuyehcf.framework.rpc.ares.util.PathUtils;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
class AresConsumerInvocationHandler implements InvocationHandler {

    private final HttpClient httpClient;
    private final RequestConfig requestConfig;

    private final List<ParamsConverter<?>> paramsConverters;
    private final List<RequestBodyConverter<?>> requestBodyConverters;
    private final List<ResponseBodyConverter<?>> responseBodyConverters;

    private final String schema;
    private final String host;
    private final int port;

    AresConsumerInvocationHandler(HttpClient httpClient, RequestConfig requestConfig,
                                  List<ParamsConverter<?>> paramsConverters,
                                  List<RequestBodyConverter<?>> requestBodyConverters,
                                  List<ResponseBodyConverter<?>> responseBodyConverters,
                                  String schema, String host, int port) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
        this.paramsConverters = paramsConverters;
        this.requestBodyConverters = requestBodyConverters;
        this.responseBodyConverters = responseBodyConverters;
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
        String acceptContentType = aresMethodAnnotation.acceptContentType();

        path = renderPath(path, method.getParameters(), args);
        HttpParams httpParams = parseParams(method.getParameters(), args);
        HttpRequestBase httpRequest = buildRequest(path, httpMethod, acceptContentType, httpParams);

        return doInvoke(httpRequest, method);
    }

    private String renderPath(String path, Parameter[] parameters, Object[] args) {
        Map<String, String> context = Maps.newHashMap();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            AresPathVariable aresPathVariable = AnnotationUtils.getAnnotation(parameter, AresPathVariable.class);

            if (aresPathVariable == null) {
                continue;
            }

            String name = aresPathVariable.name();
            context.put(name, stringEncode(args[i]));
        }

        return PathUtils.render(path, context);
    }

    private HttpParams parseParams(Parameter[] parameters, Object[] args) {
        Map<String, Object> queryParams = Maps.newHashMap();
        Map<String, Object> headers = Maps.newHashMap();

        boolean hasRequestBody = false;
        Object requestBody = null;
        String contentType = null;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            AresPathVariable aresPathVariable = AnnotationUtils.getAnnotation(parameter, AresPathVariable.class);
            AresRequestParam aresRequestParam = AnnotationUtils.getAnnotation(parameter, AresRequestParam.class);
            AresRequestHeader aresRequestHeader = AnnotationUtils.getAnnotation(parameter, AresRequestHeader.class);
            AresRequestBody aresRequestBody = AnnotationUtils.getAnnotation(parameter, AresRequestBody.class);

            int annotationNum = 0;
            if (aresPathVariable != null) {
                annotationNum++;
            }
            if (aresRequestParam != null) {
                annotationNum++;
            }
            if (aresRequestHeader != null) {
                annotationNum++;
            }
            if (aresRequestBody != null) {
                annotationNum++;
            }

            if (annotationNum > 1) {
                throw new AresException("parameter contains more than one of ares annotations");
            } else if (aresRequestParam != null) {
                if (queryParams.containsKey(aresRequestParam.name())) {
                    throw new AresException(String.format("duplicate query parameter '%s'", aresRequestParam.name()));
                }
                queryParams.put(aresRequestParam.name(), args[i]);
            } else if (aresRequestHeader != null) {
                if (headers.containsKey(aresRequestHeader.name())) {
                    throw new AresException(String.format("duplicate header '%s'", aresRequestHeader.name()));
                }
                headers.put(aresRequestHeader.name(), args[i]);
            } else if (aresRequestBody != null) {
                Assert.assertFalse(hasRequestBody, "more than one '@AresRequestBody'");
                hasRequestBody = true;
                requestBody = args[i];
                contentType = aresRequestBody.contentType();
            } else {
                if (aresPathVariable == null) {
                    throw new AresException("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'");
                }
            }
        }

        return new HttpParams(queryParams, headers, requestBody, contentType);
    }

    private HttpRequestBase buildRequest(String path, HttpMethod httpMethod, String acceptContentType, HttpParams httpParams) throws Exception {
        RequestBuilder builder = RequestBuilder.create(httpMethod.name());

        AresContext.Endpoint endpoint = AresContext.getEndpoint();

        URIBuilder uriBuilder = new URIBuilder();
        if (endpoint != null && endpoint.getSchema() != null) {
            uriBuilder.setScheme(endpoint.getSchema().name());
        } else {
            uriBuilder.setScheme(schema);
        }

        if (endpoint != null && StringUtils.isNotBlank(endpoint.getHost())) {
            uriBuilder.setHost(endpoint.getHost());
        } else {
            uriBuilder.setHost(host);
        }

        if (endpoint != null && endpoint.getPort() != null) {
            uriBuilder.setPort(endpoint.getPort());
        } else {
            uriBuilder.setPort(port);
        }

        uriBuilder.setPath(path);
        if (MapUtils.isNotEmpty(httpParams.requestParams)) {
            for (Map.Entry<String, Object> entry : httpParams.requestParams.entrySet()) {
                Object requestParam = entry.getValue();

                String content = stringEncode(requestParam);
                if (content != null) {
                    uriBuilder.addParameter(entry.getKey(), content);
                }
            }
        }

        builder.addHeader("Accept", acceptContentType);

        if (MapUtils.isNotEmpty(httpParams.requestHeaders)) {
            for (Map.Entry<String, Object> entry : httpParams.requestHeaders.entrySet()) {
                Object requestHeader = entry.getValue();

                String content = stringEncode(requestHeader);
                if (content != null) {
                    builder.addHeader(entry.getKey(), content);
                }
            }
        }

        if (httpParams.requestBody != null) {
            byte[] bytes = byteEncode(httpParams.requestBody);

            if (bytes != null) {
                ContentType contentType;
                if (StringUtils.isBlank(httpParams.contentType)) {
                    contentType = null;
                } else {
                    contentType = ContentType.parse(httpParams.contentType);
                }
                builder.setEntity(new ByteArrayEntity(bytes, contentType));
            }
        }

        builder.setUri(uriBuilder.build().toASCIIString());

        return (HttpRequestBase) builder.build();
    }

    private Object doInvoke(HttpRequestBase httpRequest, Method method) {
        httpRequest.setConfig(requestConfig);

        String url = null;
        HttpResponse response = null;
        try {
            url = httpRequest.getURI().toASCIIString();
            response = httpClient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if (HttpStatus.SC_OK != statusCode) {
                throw new AresException(String.format("http request failed, url=%s; code=%d; message=%s",
                        url,
                        statusCode,
                        EntityUtils.toString(response.getEntity())));
            }

            return byteDecode(EntityUtils.toByteArray(response.getEntity()), method.getGenericReturnType());
        } catch (AresException e) {
            throw e;
        } catch (Throwable e) {
            throw new AresException(String.format("http request error, url=%s; message=%s",
                    url, e.getMessage()), e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String stringEncode(Object obj) {
        if (obj == null) {
            return null;
        }

        if (CollectionUtils.isNotEmpty(paramsConverters)) {
            for (ParamsConverter converter : paramsConverters) {
                if (converter == null) {
                    continue;
                }
                if (!converter.match(obj, String.class)) {
                    continue;
                }
                return converter.convert(obj, String.class);
            }
        }

        return Objects.toString(obj);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private byte[] byteEncode(Object obj) {
        if (obj == null) {
            return null;
        }

        if (CollectionUtils.isNotEmpty(requestBodyConverters)) {
            for (RequestBodyConverter converter : requestBodyConverters) {
                if (converter == null) {
                    continue;
                }
                if (!converter.match(obj, byte[].class)) {
                    continue;
                }
                return converter.convert(obj, byte[].class);
            }
        }

        String text = stringEncode(obj);
        if (text == null) {
            return null;
        }
        return text.getBytes();
    }

    private Object byteDecode(byte[] bytes, Type targetType) {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }

        if (CollectionUtils.isNotEmpty(responseBodyConverters)) {
            for (ResponseBodyConverter<?> converter : responseBodyConverters) {
                if (converter == null) {
                    continue;
                }
                if (!converter.match(bytes, targetType)) {
                    continue;
                }
                return converter.convert(bytes, targetType);
            }
        }

        throw new AresException("cannot find ObjectToByteCodes compatible with '" + targetType.getTypeName() + "'");
    }

    private static final class HttpParams {
        private final Map<String, Object> requestParams;
        private final Map<String, Object> requestHeaders;
        private final Object requestBody;
        private final String contentType;

        private HttpParams(Map<String, Object> requestParams, Map<String, Object> requestHeaders, Object requestBody, String contentType) {
            this.requestParams = requestParams;
            this.requestHeaders = requestHeaders;
            this.requestBody = requestBody;
            this.contentType = contentType;
        }
    }
}
