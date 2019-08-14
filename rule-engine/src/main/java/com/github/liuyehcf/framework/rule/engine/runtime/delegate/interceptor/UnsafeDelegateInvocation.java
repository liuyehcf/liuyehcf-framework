package com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor;

/**
 * @author chenfeng.hcf
 * @date 2019/8/14
 */
public interface UnsafeDelegateInvocation extends DelegateInvocation {

    /**
     * unsafe proceed
     */
    DelegateResult unsafeProceed() throws Throwable;
}
