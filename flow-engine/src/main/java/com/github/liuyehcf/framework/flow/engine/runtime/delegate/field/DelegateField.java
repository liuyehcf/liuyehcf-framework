package com.github.liuyehcf.framework.flow.engine.runtime.delegate.field;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface DelegateField {

    /**
     * get delegate's runtime field
     *
     * @return field value
     */
    <T> T getValue();

    /**
     * set delegate's runtime field
     *
     * @param value value
     */
    <T> void setValue(T value);

    /**
     * get delegate's runtime field if not null
     * get default value if null
     *
     * @param defaultValue default value
     * @return field value
     */
    <T> T getValueOrDefault(T defaultValue);
}
