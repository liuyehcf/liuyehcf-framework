package com.github.liuyehcf.framework.rpc.ares.converters.reqbody;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class LongRequestBodyConverter extends RequestBodyConverter<Long> {

    @Override
    public byte[] convert(Long input, Type outputType) {
        return Long.toString(input).getBytes();
    }
}
