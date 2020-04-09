package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class ByteResponseBodyConverter extends ResponseBodyConverter<Byte> {

    @Override
    public Byte convert(byte[] input, Type outputType) {
        return Byte.parseByte(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (byte.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
