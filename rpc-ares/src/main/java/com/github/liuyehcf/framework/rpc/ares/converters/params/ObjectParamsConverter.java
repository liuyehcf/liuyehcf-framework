package com.github.liuyehcf.framework.rpc.ares.converters.params;

import com.github.liuyehcf.framework.rpc.ares.ParamsConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/10
 */
public class ObjectParamsConverter extends ParamsConverter<Object> {

    @Override
    public String convert(Object input, Type outputType) {
        return input.toString();
    }

    @Override
    public int order() {
        return -1024;
    }

    @Override
    protected boolean matchInputObject(Object inputObject) {
        return true;
    }
}
