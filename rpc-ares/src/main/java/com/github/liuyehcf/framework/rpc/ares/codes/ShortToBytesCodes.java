package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class ShortToBytesCodes extends ObjectToBytesCodes<Short> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (short.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Short plainObj) {
        return Short.toString(plainObj).getBytes();
    }

    @Override
    public Short decode(byte[] cipherObj, Type plainType) {
        return Short.parseShort(new String(cipherObj));
    }
}
