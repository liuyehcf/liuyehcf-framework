package com.github.liuyehcf.framework.rpc.ares.converters;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class PojoResponseBodyConverter extends ResponseBodyConverter<Object> {

    @Override
    public Object convert(byte[] input, Type outputType) {
        return JSON.parseObject(input, outputType);
    }

    @Override
    public int order() {
        return -128;
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        return true;
    }
}
