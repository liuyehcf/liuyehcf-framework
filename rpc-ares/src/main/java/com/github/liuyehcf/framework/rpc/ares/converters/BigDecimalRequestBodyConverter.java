package com.github.liuyehcf.framework.rpc.ares.converters;

import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class BigDecimalRequestBodyConverter extends RequestBodyConverter<BigDecimal> {

    @Override
    public byte[] convert(BigDecimal input, Type outputType) {
        return input.toString().getBytes();
    }
}
