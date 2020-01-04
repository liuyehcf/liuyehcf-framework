package com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor;

/**
 * @author hechenfeng
 * @date 2019/8/14
 */
public interface UnsafeDelegateInvocation extends DelegateInvocation {

    /**
     * unsafe proceed
     */
    DelegateResult unsafeProceed() throws Throwable;
}
