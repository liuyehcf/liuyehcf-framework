package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class ShortResponseBodyConverter extends ResponseBodyConverter<Short> {

    @Override
    public Short convert(byte[] input, Type outputType) {
        return Short.parseShort(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (short.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
