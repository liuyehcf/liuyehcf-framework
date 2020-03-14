package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class FloatToBytesCodes extends ObjectToBytesCodes<Float> {

    @Override
    public boolean matchPlainType(Type type) {
        if (float.class.equals(type)) {
            return true;
        }
        return super.matchPlainType(type);
    }

    @Override
    public byte[] encode(Float obj) {
        return Float.toString(obj).getBytes();
    }

    @Override
    public Float decode(byte[] obj, Type expectedPlainType) {
        return Float.parseFloat(new String(obj));
    }
}
