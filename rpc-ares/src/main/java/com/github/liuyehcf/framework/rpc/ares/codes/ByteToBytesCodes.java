package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class ByteToBytesCodes extends ObjectToBytesCodes<Byte> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (byte.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Byte plainObj) {
        return Byte.toString(plainObj).getBytes();
    }

    @Override
    public Byte decode(byte[] cipherObj, Type plainType) {
        return Byte.parseByte(new String(cipherObj));
    }
}
