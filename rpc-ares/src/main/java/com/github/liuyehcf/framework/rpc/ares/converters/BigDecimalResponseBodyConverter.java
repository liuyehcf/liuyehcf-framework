package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BigDecimalResponseBodyConverter extends ResponseBodyConverter<BigDecimal> {

    @Override
    public BigDecimal convert(byte[] input, Type outputType) {
        return new BigDecimal(new String(input));
    }
}
