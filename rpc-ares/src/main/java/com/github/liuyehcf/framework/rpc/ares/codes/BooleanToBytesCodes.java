package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BooleanToBytesCodes extends ObjectToBytesCodes<Boolean> {

    @Override
    public boolean matchDecodeType(Type expectedPlainType) {
        if (boolean.class.equals(expectedPlainType)) {
            return true;
        }
        return super.matchDecodeType(expectedPlainType);
    }

    @Override
    public byte[] encode(Boolean obj) {
        return Boolean.toString(obj).getBytes();
    }

    @Override
    public Boolean decode(byte[] obj, Type expectedPlainType) {
        return Boolean.parseBoolean(new String(obj));
    }
}
