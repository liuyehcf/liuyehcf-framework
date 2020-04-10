package com.github.liuyehcf.framework.rpc.ares.converters.resbody;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BigIntegerResponseBodyConverter extends ResponseBodyConverter<BigInteger> {

    @Override
    public BigInteger convert(byte[] input, Type outputType) {
        return new BigInteger(new String(input));
    }
}
