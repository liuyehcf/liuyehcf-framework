package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class BooleanToBytesCodes extends ObjectToBytesCodes<Boolean> {

    @Override
    public boolean matchDecode(Type type) {
        if (boolean.class.equals(type)) {
            return true;
        }
        return super.matchDecode(type);
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
