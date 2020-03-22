package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/22
 */
public class StringToBytesCodes extends ObjectToBytesCodes<String> {

    @Override
    public byte[] encode(String plainObj) {
        return plainObj.getBytes();
    }

    @Override
    public String decode(byte[] cipherObj, Type plainType) {
        return new String(cipherObj);
    }
}
