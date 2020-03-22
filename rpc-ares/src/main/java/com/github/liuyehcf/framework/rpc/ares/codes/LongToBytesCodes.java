package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class LongToBytesCodes extends ObjectToBytesCodes<Long> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (long.class.equals(plainType)) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Long plainObj) {
        return Long.toString(plainObj).getBytes();
    }

    @Override
    public Long decode(byte[] cipherObj, Type plainType) {
        return Long.parseLong(new String(cipherObj));
    }
}
