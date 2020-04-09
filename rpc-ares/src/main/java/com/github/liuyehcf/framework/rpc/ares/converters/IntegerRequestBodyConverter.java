package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class IntegerRequestBodyConverter extends RequestBodyConverter<Integer> {

    @Override
    public byte[] convert(Integer input, Type outputType) {
        return Integer.toString(input).getBytes();
    }
}
