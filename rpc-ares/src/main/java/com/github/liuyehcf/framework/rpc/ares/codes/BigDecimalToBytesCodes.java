package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BigDecimalToBytesCodes extends ObjectToBytesCodes<BigDecimal> {

    @Override
    public byte[] encode(BigDecimal obj) {
        return obj.toString().getBytes();
    }

    @Override
    public BigDecimal decode(byte[] obj, Type expectedPlainType) {
        return new BigDecimal(new String(obj));
    }
}
