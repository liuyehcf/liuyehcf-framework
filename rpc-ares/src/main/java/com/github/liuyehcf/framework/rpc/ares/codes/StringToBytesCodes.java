package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/22
 */
public class StringToBytesCodes extends ObjectToBytesCodes<String> {

    @Override
    public byte[] encode(String obj) {
        return obj.getBytes();
    }

    @Override
    public String decode(byte[] obj, Type expectedPlainType) {
        return new String(obj);
    }
}
