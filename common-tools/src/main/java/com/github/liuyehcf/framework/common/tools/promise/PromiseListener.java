package com.github.liuyehcf.framework.common.tools.promise;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public interface PromiseListener<T> {

    /**
     * this method must be trigger in all cases
     *
     * @param promise the very promise
     * @throws Throwable throwable
     */
    void operationComplete(Promise<T> promise) throws Throwable;
}
