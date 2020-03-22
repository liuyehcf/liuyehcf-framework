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
    public boolean matchDecode(Type type) {
        return true;
    }

    @Override
    public boolean matchEncode(Object obj) {
        return true;
    }

    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Object decode(byte[] obj, Type expectedPlainType) {
        return JSON.parseObject(obj, expectedPlainType);
    }
}
