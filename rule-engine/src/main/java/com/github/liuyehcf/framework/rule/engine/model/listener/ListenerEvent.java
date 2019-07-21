package com.github.liuyehcf.framework.rule.engine.model.listener;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public enum ListenerEvent {

    /**
     * execute before specified element
     */
    before,

    /**
     * execute after specified element execution succeeded
     * if the element is condition, execution status is success if condition do not throw exception, regardless of whether condition result is true of false
     */
    success,

    /**
     * execute after specified element execution failed
     */
    failure
}
