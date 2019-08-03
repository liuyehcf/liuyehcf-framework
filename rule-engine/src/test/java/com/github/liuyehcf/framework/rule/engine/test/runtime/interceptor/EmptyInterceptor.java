package com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateResult;

/**
 * @author hechenfeng
 * @date 2019/5/5
 */
public class EmptyInterceptor implements DelegateInterceptor {
    @Override
    public DelegateResult invoke(DelegateInvocation delegateInvocation) throws Throwable {
        return delegateInvocation.proceed();
    }
}
