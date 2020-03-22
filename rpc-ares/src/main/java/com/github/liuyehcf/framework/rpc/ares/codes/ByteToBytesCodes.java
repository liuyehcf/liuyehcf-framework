package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class ByteToBytesCodes extends ObjectToBytesCodes<Byte> {

    @Override
    public boolean matchDecodeType(Type type) {
        if (byte.class.equals(type)) {
            return true;
        }
        return super.matchDecodeType(type);
    }

    @Override
    public byte[] encode(Byte obj) {
        return Byte.toString(obj).getBytes();
    }

    @Override
    public Byte decode(byte[] obj, Type expectedPlainType) {
        return Byte.parseByte(new String(obj));
    }
}
