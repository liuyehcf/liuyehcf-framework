package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class LongResponseBodyConverter extends ResponseBodyConverter<Long> {

    @Override
    public Long convert(byte[] input, Type outputType) {
        return Long.parseLong(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (long.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
