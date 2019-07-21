package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

/**
 * @author hechenfeng
 * @date 2019/5/12
 */
public class DefaultAttribute implements Attribute {

    private final String name;
    private final Object value;

    public DefaultAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getValue() {
        return (T) value;
    }

    @Override
    public String toString() {
        return "DefaultAttribute{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
