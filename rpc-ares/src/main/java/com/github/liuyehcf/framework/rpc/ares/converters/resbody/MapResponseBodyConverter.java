package com.github.liuyehcf.framework.rpc.ares.converters.resbody;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class MapResponseBodyConverter extends ResponseBodyConverter<Map<?, ?>> {

    @Override
    public Map<?, ?> convert(byte[] input, Type outputType) {
        return JSON.parseObject(input, outputType);
    }

    @Override
    protected boolean matchOutputType(Type outputType) {
        if (outputType.getTypeName().startsWith(Map.class.getName())) {
            return true;
        }
        return super.matchOutputType(outputType);
    }
}
