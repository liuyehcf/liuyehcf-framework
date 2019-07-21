package com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface Factory<T> {

    /**
     * create object
     *
     * @return target object
     */
    T create();
}
