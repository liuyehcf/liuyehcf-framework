package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
public interface PropertyUpdate {

    /**
     * property update type
     *
     * @see PropertyUpdateType
     */
    PropertyUpdateType getType();

    /**
     * property name
     */
    String getName();

    /**
     * old property value
     */
    Object getOldValue();

    /**
     * new property value
     */
    Object getNewValue();
}
