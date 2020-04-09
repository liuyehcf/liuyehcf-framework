package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public abstract class ResponseBodyConverter<O> extends Converter<byte[], O> {

    @Override
    public final boolean match(Object inputObject, Type outputType) {
        return matchOutputType(outputType);
    }

    @Override
    public abstract O convert(byte[] input, Type outputType);

    @Override
    public int order() {
        return 0;
    }

    /**
     * whether outputType matches this converter
     *
     * @param outputType type of output
     */
    protected boolean matchOutputType(Type outputType) {
        return Objects.equals(super.outputType, outputType);
    }
}
