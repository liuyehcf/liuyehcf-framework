package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class FloatToBytesCodes extends ObjectToBytesCodes<Float> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (float.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Float plainObj) {
        return Float.toString(plainObj).getBytes();
    }

    @Override
    public Float decode(byte[] cipherObj, Type plainType) {
        return Float.parseFloat(new String(cipherObj));
    }
}
