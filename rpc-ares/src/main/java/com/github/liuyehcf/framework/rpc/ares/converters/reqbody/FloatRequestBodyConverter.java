package com.github.liuyehcf.framework.rpc.ares.converters.reqbody;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class FloatRequestBodyConverter extends RequestBodyConverter<Float> {

    @Override
    public byte[] convert(Float input, Type outputType) {
        return Float.toString(input).getBytes();
    }
}
