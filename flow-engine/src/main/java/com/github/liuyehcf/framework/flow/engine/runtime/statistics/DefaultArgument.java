package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

/**
 * @author hechenfeng
 * @date 2019/5/12
 */
public class DefaultArgument implements Argument {

    private final String name;
    private final Object value;

    public DefaultArgument(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DefaultArgument{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
