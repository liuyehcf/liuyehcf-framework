package com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor;

/**
 * @author hechenfeng
 * @date 2019/7/31
 */
public class DefaultDelegateResult implements DelegateResult {

    private final boolean isAsync;
    private final Object result;
    private final DelegatePromise delegatePromise;

    DefaultDelegateResult(boolean isAsync, Object result, DelegatePromise delegatePromise) {
        this.isAsync = isAsync;
        this.result = result;
        this.delegatePromise = delegatePromise;
    }

    @Override
    public boolean isAsync() {
        return isAsync;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public DelegatePromise getDelegatePromise() {
        return delegatePromise;
    }
}
