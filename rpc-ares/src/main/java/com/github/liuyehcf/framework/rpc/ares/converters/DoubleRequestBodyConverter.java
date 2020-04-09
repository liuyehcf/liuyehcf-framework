package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class DoubleRequestBodyConverter extends RequestBodyConverter<Double> {

    @Override
    public byte[] convert(Double input, Type outputType) {
        return Double.toString(input).getBytes();
    }
}
