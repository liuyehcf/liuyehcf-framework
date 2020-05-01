package com.github.liuyehcf.framework.rpc.ares;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.lang.reflect.Method;

/**
 * @author hechenfeng
 * @date 2020/5/1
 */
public abstract class ResponseHandler implements Comparable<ResponseHandler> {

    @Override
    public final int compareTo(ResponseHandler o) {
        return Integer.compare(o.order(), this.order());
    }

    /**
     * whether httpRequest and httpResponse matches this handler
     *
     * @param request  http request
     * @param response http response
     * @return whether matches
     */
    public abstract boolean match(HttpUriRequest request, HttpResponse response, Method method);

    /**
     * @param request  http request
     * @param response http response
     * @return result
     */
    public abstract Object process(HttpUriRequest request, HttpResponse response, Method method) throws Exception;

    /**
     * order of this converter
     */
    public int order() {
        return 0;
    }
}
