package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class StringResponseBodyConverter extends ResponseBodyConverter<String> {

    @Override
    public String convert(byte[] input, Type outputType) {
        return new String(input);
    }
}
