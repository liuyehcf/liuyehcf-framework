package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.rpc.ares.*;
import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.ares.constant.SchemaType;
import com.github.liuyehcf.framework.rpc.ares.util.AresContext;
import com.github.liuyehcf.framework.rpc.ares.util.PathUtils;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
class AresConsumerInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AresConsumerInvocationHandler.class);

    private final HttpClient httpClient;
    private final RequestConfig requestConfig;

    private final List<ParamsConverter<?>> paramsConverters;
    private final List<RequestBodyConverter<?>> requestBodyConverters;
    private final List<ResponseBodyConverter<?>> responseBodyConverters;
    private final List<ResponseHandler> responseHandlers;

    private final String schema;
    private final String host;
    private final int port;

    AresConsumerInvocationHandler(HttpClient httpClient, RequestConfig requestConfig,
                                  List<ParamsConverter<?>> paramsConverters,
                                  List<RequestBodyConverter<?>> requestBodyConverters,
                                  List<ResponseBodyConverter<?>> responseBodyConverters,
                                  List<ResponseHandler> responseHandlers,
                                  String schema, String host, int port) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
        this.paramsConverters = paramsConverters;
        this.requestBodyConverters = requestBodyConverters;
        this.responseBodyConverters = responseBodyConverters;
        this.responseHandlers = responseHandlers;
        this.responseHandlers.add(this.new DefaultResponseHandler());
        Collections.sort(this.responseHandlers);
        this.schema = schema;
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AresMethod aresMethodAnnotation = AnnotationUtils.getAnnotation(method, AresMethod.class);
        if (aresMethodAnnotation == null) {
            throw new AresException("http consumer missing AresMethod");
        }

        String path = aresMethodAnnotation.path();
        HttpMethod httpMethod = aresMethodAnnotation.method();
        String acceptContentType = aresMethodAnnotation.acceptContentType();

        path = renderPath(path, method.getParameters(), args);
        HttpParams httpParams = parseParams(method.getParameters(), args);
        HttpRequestBase request = buildRequest(path, httpMethod, acceptContentType, httpParams);

        return doInvoke(request, method);
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
            context.put(name, convertParamToString(args[i]));
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
                if (hasRequestBody) {
                    throw new AresException("more than one '@AresRequestBody'");
                }
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

                String content = convertParamToString(requestParam);
                if (content != null) {
                    uriBuilder.addParameter(entry.getKey(), content);
                }
            }
        }

        builder.addHeader(HttpHeaders.ACCEPT, acceptContentType);
        if (SchemaType.http.name().equalsIgnoreCase(schema) && port == 80
                || SchemaType.https.name().equalsIgnoreCase(schema) && port == 443) {
            builder.addHeader(HttpHeaders.HOST, uriBuilder.getHost());
        } else {
            builder.addHeader(HttpHeaders.HOST, String.format("%s:%d", uriBuilder.getHost(), uriBuilder.getPort()));
        }

        if (MapUtils.isNotEmpty(httpParams.requestHeaders)) {
            for (Map.Entry<String, Object> entry : httpParams.requestHeaders.entrySet()) {
                Object requestHeader = entry.getValue();

                String content = convertParamToString(requestHeader);
                if (content != null) {
                    builder.addHeader(entry.getKey(), content);
                }
            }
        }

        if (httpParams.requestBody != null) {
            byte[] bytes = convertRequestBodyToBytes(httpParams.requestBody);

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

    private Object doInvoke(HttpRequestBase request, Method method) {
        request.setConfig(requestConfig);

        String uri = null;
        HttpResponse response = null;
        try {
            uri = request.getURI().toASCIIString();
            response = httpClient.execute(request);

            for (ResponseHandler responseHandler : responseHandlers) {
                if (responseHandler.match(request, response, method)) {
                    return responseHandler.process(request, response, method);
                }
            }

            throw new AresException(String.format("missing response handler, uri=%s", uri));
        } catch (AresException e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.error("http request error, url={}; message={}", uri, e.getMessage(), e);
            throw new AresException(String.format("http request error, url=%s; message=%s",
                    uri, e.getMessage()), e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String convertParamToString(Object obj) {
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

        throw new AresException(String.format("no ParamsConverter matches type %s", obj.getClass().getName()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private byte[] convertRequestBodyToBytes(Object obj) {
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

        throw new AresException(String.format("no RequestBodyConverter matches type %s", obj.getClass().getName()));
    }

    private Object convertBytesToResponseBody(byte[] bytes, Type targetType) {
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

        throw new AresException(String.format("no ResponseBodyConverter matches type %s", targetType.getTypeName()));
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

    private final class DefaultResponseHandler extends ResponseHandler {

        @Override
        public boolean match(HttpUriRequest request, HttpResponse response, Method method) {
            return true;
        }

        @Override
        public Object process(HttpUriRequest request, HttpResponse response, Method method) throws Exception {
            String uri = request.getURI().toASCIIString();
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (HttpStatus.SC_OK != statusCode) {
                throw new AresException(String.format("http request failed, uri=%s; code=%d; reasonPhrase=%s; message=%s",
                        uri,
                        statusCode,
                        reasonPhrase,
                        EntityUtils.toString(response.getEntity())));
            }

            byte[] responseBody;
            if (response.getEntity() == null) {
                responseBody = new byte[0];
            } else {
                responseBody = EntityUtils.toByteArray(response.getEntity());
            }
            return convertBytesToResponseBody(responseBody, method.getGenericReturnType());
        }

        @Override
        public int order() {
            return -1024;
        }
    }
}
