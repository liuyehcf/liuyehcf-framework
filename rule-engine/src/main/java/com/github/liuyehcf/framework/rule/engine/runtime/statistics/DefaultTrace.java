package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class DefaultTrace implements Trace {

    /**
     * executionId
     */
    private final long executionId;

    /**
     * unique within rule
     */
    private final String id;

    /**
     * element's type
     *
     * @see com.github.liuyehcf.framework.rule.engine.model.ElementType
     */
    private final ElementType type;

    /**
     * executable's name
     */
    private final String name;

    /**
     * executable's argument name and value
     * null if element is not executable
     */
    private final List<Argument> arguments;

    /**
     * variable updated by executable
     * null if element is not executable
     */
    private final List<PropertyUpdate> propertyUpdates;

    /**
     * trace attributes
     */
    private final Map<String, Attribute> attributes;

    /**
     * start nanos
     */
    private final long startNanos;

    /**
     * executable's result
     */
    private volatile Object result;

    /**
     * cause
     */
    private volatile Throwable cause;

    /**
     * end nanos
     */
    private volatile long endNanos;

    public DefaultTrace(long executionId, String id, ElementType type, String name, List<Argument> arguments, Object result,
                        List<PropertyUpdate> propertyUpdates, Map<String, Attribute> attributes, Throwable cause,
                        long startNanos, long endNanos) {
        Assert.assertNotNull(id);
        Assert.assertNotNull(type);

        this.executionId = executionId;
        this.id = id;
        this.type = type;
        this.name = name;
        this.arguments = arguments;
        this.result = result;
        this.propertyUpdates = propertyUpdates;
        this.attributes = attributes;
        this.cause = cause;
        this.startNanos = startNanos;
        this.endNanos = endNanos;
    }

    @Override
    public final long getExecutionId() {
        return executionId;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final ElementType getType() {
        return type;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final List<Argument> getArguments() {
        return arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getResult() {
        return (T) result;
    }

    public final void setResult(Object result) {
        this.result = result;
    }

    @Override
    public final List<PropertyUpdate> getPropertyUpdates() {
        return propertyUpdates;
    }

    @Override
    public final Map<String, Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public final Throwable getCause() {
        return cause;
    }

    public final void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public final long getStartNanos() {
        return startNanos;
    }

    @Override
    public final long getEndNanos() {
        return endNanos;
    }

    public final void setEndNanos(long endNanos) {
        this.endNanos = endNanos;
    }

    @Override
    public String toString() {
        return String.format("trace(id=%s, type=%s, name=%s)", id, type, name);
    }
}
