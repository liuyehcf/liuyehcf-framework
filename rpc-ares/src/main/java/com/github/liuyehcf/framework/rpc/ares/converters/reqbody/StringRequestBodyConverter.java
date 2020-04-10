package com.github.liuyehcf.framework.rpc.ares.converters.reqbody;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class StringRequestBodyConverter extends RequestBodyConverter<String> {

    @Override
    public byte[] convert(String input, Type outputType) {
        return input.getBytes();
    }
}
