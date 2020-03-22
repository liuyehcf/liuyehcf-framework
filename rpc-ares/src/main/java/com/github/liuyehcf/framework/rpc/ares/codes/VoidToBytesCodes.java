package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class VoidToBytesCodes extends ObjectToBytesCodes<Void> {

    @Override
    public boolean matchDecodeType(Type type) {
        if (void.class.equals(type)) {
            return true;
        }
        return super.matchDecodeType(type);
    }

    @Override
    public byte[] encode(Void obj) {
        return null;
    }

    @Override
    public Void decode(byte[] obj, Type expectedPlainType) {
        return null;
    }
}
