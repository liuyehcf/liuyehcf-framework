package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@Component
@Order(value = 1)
public class DelegateInterceptorOrder1 implements DelegateInterceptor {
    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        System.out.println("enter DelegateInterceptorOrder1");

        Object proceed = delegateInvocation.proceed();

        System.out.println("exit DelegateInterceptorOrder1");
        return proceed;
    }
}