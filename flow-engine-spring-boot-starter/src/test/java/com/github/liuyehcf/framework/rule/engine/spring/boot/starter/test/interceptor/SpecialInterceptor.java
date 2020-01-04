package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test.interceptor;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.InterceptorBean;

/**
 * @author hechenfeng
 * @date 2019/10/19
 */
@InterceptorBean(engineNameRegex = "specialRuleEngine")
public class SpecialInterceptor implements DelegateInterceptor {

    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        System.out.println(getClass().getName());
        return delegateInvocation.proceed();
    }
}
