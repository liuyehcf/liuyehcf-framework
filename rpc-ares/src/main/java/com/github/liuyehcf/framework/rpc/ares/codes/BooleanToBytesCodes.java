package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BooleanToBytesCodes extends ObjectToBytesCodes<Boolean> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (boolean.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Boolean plainObj) {
        return Boolean.toString(plainObj).getBytes();
    }

    @Override
    public Boolean decode(byte[] cipherObj, Type plainType) {
        return Boolean.parseBoolean(new String(cipherObj));
    }
}
