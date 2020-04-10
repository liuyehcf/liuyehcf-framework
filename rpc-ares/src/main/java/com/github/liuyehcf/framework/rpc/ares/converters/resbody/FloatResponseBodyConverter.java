package com.github.liuyehcf.framework.rpc.ares.converters.resbody;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class FloatResponseBodyConverter extends ResponseBodyConverter<Float> {

    @Override
    public Float convert(byte[] input, Type outputType) {
        return Float.parseFloat(new String(input));
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (float.class.equals(outputType)) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
