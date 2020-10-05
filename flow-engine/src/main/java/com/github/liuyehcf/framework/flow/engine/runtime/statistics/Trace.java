package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

import com.github.liuyehcf.framework.flow.engine.model.ElementType;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
public interface Trace {

    /**
     * execution id
     */
    long getExecutionId();

    /**
     * element id
     */
    String getId();

    /**
     * element's type
     *
     * @see com.github.liuyehcf.framework.flow.engine.model.ElementType
     */
    ElementType getType();

    /**
     * executable's name
     */
    String getName();

    /**
     * executable's argument name and value
     * null if element is not executable
     */
    List<Argument> getArguments();

    /**
     * element result
     * only condition has result type boolean, otherwise null
     *
     * @param <T> result type
     * @return result
     */
    <T> T getResult();

    /**
     * variable updated by executable
     * null if element is not executable
     */
    List<PropertyUpdate> getPropertyUpdates();

    /**
     * attributes
     */
    Map<String, Attribute> getAttributes();

    /**
     * get cause
     *
     * @return cause
     */
    Throwable getCause();

    /**
     * start timestamp
     */
    long getStartTimestamp();

    /**
     * end timestamp
     */
    long getEndTimestamp();

    /**
     * use time in nano seconds
     */
    long getUseTimeNano();
}
