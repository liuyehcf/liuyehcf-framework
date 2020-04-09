package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BooleanResponseBodyConverter extends ResponseBodyConverter<Boolean> {

    @Override
    public Boolean convert(byte[] input, Type outputType) {
        return Boolean.parseBoolean(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (boolean.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
