package com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.AbstractRegexpDelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class ActionRegexInterceptor extends AbstractRegexpDelegateInterceptor {

    public static AtomicLong COUNTER = new AtomicLong(0);

    @Override
    protected String getRegexExpression() {
        return ".*Action";
    }

    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        COUNTER.incrementAndGet();

        return delegateInvocation.proceed();
    }
}
