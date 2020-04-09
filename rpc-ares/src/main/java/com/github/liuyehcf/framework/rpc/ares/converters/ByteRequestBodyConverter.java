package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class ByteRequestBodyConverter extends RequestBodyConverter<Byte> {

    @Override
    public byte[] convert(Byte input, Type outputType) {
        return Byte.toString(input).getBytes();
    }
}
