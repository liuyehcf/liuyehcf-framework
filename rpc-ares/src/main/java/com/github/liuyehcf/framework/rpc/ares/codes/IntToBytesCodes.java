package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class IntToBytesCodes extends ObjectToBytesCodes<Integer> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (int.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Integer plainObj) {
        return Integer.toString(plainObj).getBytes();
    }

    @Override
    public Integer decode(byte[] cipherObj, Type plainType) {
        return Integer.parseInt(new String(cipherObj));
    }
}
