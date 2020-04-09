package com.github.liuyehcf.framework.rpc.ares.converters;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class PojoRequestBodyConverter extends RequestBodyConverter<Object> {

    @Override
    public byte[] convert(Object input, Type outputType) {
        return JSON.toJSONBytes(input);
    }

    @Override
    public int order() {
        return -128;
    }

    @Override
    protected boolean matchInputObject(Object inputObject) {
        return true;
    }
}
