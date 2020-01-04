package com.github.liuyehcf.framework.flow.engine.runtime.delegate.field;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class DefaultDelegateField implements DelegateField {

    private Object value;

    public DefaultDelegateField(Object value) {
        this.value = value;
    }

    @Override
    public final <T> T getValue() {
        return getValueOrDefault(null);
    }

    @Override
    public final <T> void setValue(T value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValueOrDefault(T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return (T) value;
    }
}
