package com.github.liuyehcf.framework.rpc.ares.converters.resbody;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BytesResponseBodyConverter extends ResponseBodyConverter<byte[]> {

    @Override
    public byte[] convert(byte[] input, Type outputType) {
        return input;
    }
}
