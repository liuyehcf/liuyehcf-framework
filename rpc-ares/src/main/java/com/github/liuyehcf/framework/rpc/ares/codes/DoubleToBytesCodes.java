package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class DoubleToBytesCodes extends ObjectToBytesCodes<Double> {

    @Override
    public boolean matchDecodeType(Type type) {
        if (double.class.equals(type)) {
            return true;
        }
        return super.matchDecodeType(type);
    }

    @Override
    public byte[] encode(Double obj) {
        return Double.toString(obj).getBytes();
    }

    @Override
    public Double decode(byte[] obj, Type expectedPlainType) {
        return Double.parseDouble(new String(obj));
    }
}
