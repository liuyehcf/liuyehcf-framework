package com.github.liuyehcf.framework.rpc.ares.test.handler;

import com.github.liuyehcf.framework.rpc.ares.ResponseHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hechenfeng
 * @date 2020/5/1
 */
@Component
public class Status404Handler extends ResponseHandler {

    @Override
    public boolean match(HttpUriRequest request, HttpResponse response, Method method) {
        return response.getStatusLine().getStatusCode() == 404;
    }

    @Override
    public Object process(HttpUriRequest request, HttpResponse response, Method method) {
        throw new RuntimeException("not found");
    }
}
