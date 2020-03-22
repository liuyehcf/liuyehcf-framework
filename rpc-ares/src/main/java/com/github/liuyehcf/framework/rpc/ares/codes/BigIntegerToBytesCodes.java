package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BigIntegerToBytesCodes extends ObjectToBytesCodes<BigInteger> {

    @Override
    public byte[] encode(BigInteger plainObj) {
        return plainObj.toString().getBytes();
    }

    @Override
    public BigInteger decode(byte[] cipherObj, Type plainType) {
        return new BigInteger(new String(cipherObj));
    }
}
