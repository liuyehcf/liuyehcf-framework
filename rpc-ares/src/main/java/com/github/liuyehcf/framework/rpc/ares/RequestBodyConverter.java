package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public abstract class RequestBodyConverter<I> extends Converter<I, byte[]> {

    @Override
    public final boolean match(Object inputObject, Type outputType) {
        return matchInputObject(inputObject);
    }

    @Override
    public abstract byte[] convert(I input, Type outputType);

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
