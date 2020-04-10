package com.github.liuyehcf.framework.rpc.ares.converters.reqbody;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class ShortRequestBodyConverter extends RequestBodyConverter<Short> {

    @Override
    public byte[] convert(Short input, Type outputType) {
        return Short.toString(input).getBytes();
    }
}
