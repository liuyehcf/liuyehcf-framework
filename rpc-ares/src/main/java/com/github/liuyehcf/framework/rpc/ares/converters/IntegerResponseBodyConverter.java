package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class IntegerResponseBodyConverter extends ResponseBodyConverter<Integer> {

    @Override
    public Integer convert(byte[] input, Type outputType) {
        return Integer.parseInt(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (int.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
