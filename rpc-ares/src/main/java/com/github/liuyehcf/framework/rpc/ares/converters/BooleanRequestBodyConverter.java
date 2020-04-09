package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BooleanRequestBodyConverter extends RequestBodyConverter<Boolean> {

    @Override
    public byte[] convert(Boolean input, Type outputType) {
        return Boolean.toString(input).getBytes();
    }
}
