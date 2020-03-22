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
    public byte[] encode(BigDecimal plainObj) {
        return plainObj.toString().getBytes();
    }

    @Override
    public BigDecimal decode(byte[] cipherObj, Type plainType) {
        return new BigDecimal(new String(cipherObj));
    }
}
