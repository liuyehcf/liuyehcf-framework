package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class DoubleResponseBodyConverter extends ResponseBodyConverter<Double> {

    @Override
    public Double convert(byte[] input, Type outputType) {
        return Double.parseDouble(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (double.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
