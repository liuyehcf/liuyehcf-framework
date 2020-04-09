package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BytesRequestBodyConverter extends RequestBodyConverter<byte[]> {

    @Override
    public byte[] convert(byte[] input, Type outputType) {
        return input;
    }
}
