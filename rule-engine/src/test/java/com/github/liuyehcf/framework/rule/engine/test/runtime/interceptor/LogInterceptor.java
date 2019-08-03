package com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateResult;

/**
 * @author hechenfeng
 * @date 2019/5/5
 */
public class LogInterceptor implements DelegateInterceptor {
    @Override
    public DelegateResult invoke(DelegateInvocation delegateInvocation) throws Throwable {
        try {
            System.err.println("[LOG-BEGIN]");
            System.err.println(String.format("     - %-15s - %s", "name", delegateInvocation.getExecutableContext().getName()));
            System.err.println(String.format("     - %-15s - %s", "type", delegateInvocation.getType()));
            System.err.println(String.format("     - %-15s - %s", "argumentName", JSON.toJSONString(delegateInvocation.getArgumentNames())));
            System.err.println(String.format("     - %-15s - %s", "argumentValue", JSON.toJSONString(delegateInvocation.getArgumentValues())));
            return delegateInvocation.proceed();
        } finally {
            System.err.println(String.format("     - %-15s - %s", "attributes", JSON.toJSONString(delegateInvocation.getExecutableContext().getLocalAttributes())));
            System.err.println("[LOG-END]");
        }
    }
}