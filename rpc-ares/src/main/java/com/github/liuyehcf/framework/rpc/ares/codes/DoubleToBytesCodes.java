package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class DoubleToBytesCodes extends ObjectToBytesCodes<Double> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (double.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Double plainObj) {
        return Double.toString(plainObj).getBytes();
    }

    @Override
    public Double decode(byte[] cipherObj, Type plainType) {
        return Double.parseDouble(new String(cipherObj));
    }
}
