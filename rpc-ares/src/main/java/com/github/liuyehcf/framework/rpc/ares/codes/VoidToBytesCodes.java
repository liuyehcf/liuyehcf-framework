package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class VoidToBytesCodes extends ObjectToBytesCodes<Void> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (void.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Void plainObj) {
        return null;
    }

    @Override
    public Void decode(byte[] cipherObj, Type plainType) {
        return null;
    }
}
