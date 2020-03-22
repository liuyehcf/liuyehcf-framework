package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class IntToBytesCodes extends ObjectToBytesCodes<Integer> {

    @Override
    public boolean matchDecodeType(Type expectedPlainType) {
        if (int.class.equals(expectedPlainType)) {
            return true;
        }
        return super.matchDecodeType(expectedPlainType);
    }

    @Override
    public byte[] encode(Integer obj) {
        return Integer.toString(obj).getBytes();
    }

    @Override
    public Integer decode(byte[] obj, Type expectedPlainType) {
        return Integer.parseInt(new String(obj));
    }
}
