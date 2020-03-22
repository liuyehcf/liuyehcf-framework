package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BytesToBytesCodes extends ObjectToBytesCodes<byte[]> {

    @Override
    public byte[] encode(byte[] plainObj) {
        return plainObj;
    }

    @Override
    public byte[] decode(byte[] cipherObj, Type plainType) {
        return cipherObj;
    }
}
