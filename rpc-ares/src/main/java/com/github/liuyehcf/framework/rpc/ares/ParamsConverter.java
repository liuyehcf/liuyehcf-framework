package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public abstract class ParamsConverter<I> extends Converter<I, String> {

    @Override
    public final boolean match(Object inputObject, Type outputType) {
        return matchInputObject(inputObject);
    }

    @Override
    public abstract String convert(I input, Type outputType);

    @Override
    public int order() {
        return 0;
    }

    /**
     * whether inputObject matches this converter
     *
     * @param inputObject input object
     */
    protected boolean matchInputObject(Object inputObject) {
        return inputMatcher.match(inputObject);
    }
}
