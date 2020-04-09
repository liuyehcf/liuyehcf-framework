package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BigIntegerRequestBodyConverter extends RequestBodyConverter<BigInteger> {

    @Override
    public byte[] convert(BigInteger input, Type outputType) {
        return input.toString().getBytes();
    }
}
