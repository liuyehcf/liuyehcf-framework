package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class VoidRequestBodyConverter extends RequestBodyConverter<Void> {

    @Override
    public byte[] convert(Void input, Type outputType) {
        return null;
    }
}
