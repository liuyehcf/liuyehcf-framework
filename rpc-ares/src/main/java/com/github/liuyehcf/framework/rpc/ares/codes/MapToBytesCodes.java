package com.github.liuyehcf.framework.rpc.ares.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class MapToBytesCodes extends ObjectToBytesCodes<Map<String, ?>> {

    @Override
    public boolean matchDecodeType(Type plainType) {
        if (plainType.getTypeName().startsWith(Map.class.getName())) {
            return true;
        }
        return super.matchDecodeType(plainType);
    }

    @Override
    public byte[] encode(Map<String, ?> plainObj) {
        return JSON.toJSONBytes(plainObj);
    }

    @Override
    public Map<String, ?> decode(byte[] cipherObj, Type plainType) {
        return JSON.parseObject(cipherObj, plainType);
    }
}
