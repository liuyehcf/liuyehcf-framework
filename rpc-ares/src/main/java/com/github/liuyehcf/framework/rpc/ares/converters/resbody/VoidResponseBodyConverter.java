package com.github.liuyehcf.framework.rpc.ares.converters.resbody;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class VoidResponseBodyConverter extends ResponseBodyConverter<Void> {

    @Override
    public Void convert(byte[] input, Type outputType) {
        return null;
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (void.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
