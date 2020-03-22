package com.github.liuyehcf.framework.rpc.ares.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/22
 */
public class PojoToBytesCodes extends ObjectToBytesCodes<Object> {

    @Override
    public int order() {
        return -128;
    }

    @Override
    public boolean matchDecodeType(Type plainType) {
        return true;
    }

    @Override
    public boolean matchEncodeObject(Object plainObj) {
        return true;
    }

    @Override
    public byte[] encode(Object plainObj) {
        return JSON.toJSONBytes(plainObj);
    }

    @Override
    public Object decode(byte[] cipherObj, Type plainType) {
        return JSON.parseObject(cipherObj, plainType);
    }
}
