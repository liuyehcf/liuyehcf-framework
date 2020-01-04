package com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor;

/**
 * @author hechenfeng
 * @date 2019/7/31
 */
public interface DelegateResult {

    /**
     * is the execution process asynchronous
     */
    boolean isAsync();

    /**
     * get execution result
     * if async, always return null
     *
     * @return execution result
     */
    Object getResult();

    /**
     * If executable is executed asynchronously, the method returns promise for asynchronous execution.
     *
     * @return promise, return 'null' if not async
     */
    DelegatePromise getDelegatePromise();
}
