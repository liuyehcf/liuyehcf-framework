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
    public boolean matchPlainType(Type type) {
        if (type.getTypeName().startsWith(Map.class.getName())) {
            return true;
        }
        return super.matchPlainType(type);
    }

    @Override
    public byte[] encode(Map<String, ?> obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Map<String, ?> decode(byte[] obj, Type expectedPlainType) {
        return JSON.parseObject(obj, expectedPlainType);
    }
}
