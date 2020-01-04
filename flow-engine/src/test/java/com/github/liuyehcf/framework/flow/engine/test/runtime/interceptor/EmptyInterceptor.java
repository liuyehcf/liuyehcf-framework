package com.github.liuyehcf.framework.flow.engine.test.runtime.interceptor;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;

/**
 * @author hechenfeng
 * @date 2019/5/5
 */
public class EmptyInterceptor implements DelegateInterceptor {
    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        return delegateInvocation.proceed();
    }
}
