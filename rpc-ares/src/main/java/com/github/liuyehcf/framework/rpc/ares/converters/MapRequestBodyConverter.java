package com.github.liuyehcf.framework.rpc.ares.converters;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public class MapRequestBodyConverter extends RequestBodyConverter<Map<?, ?>> {

    @Override
    public byte[] convert(Map<?, ?> input, Type outputType) {
        return JSON.toJSONBytes(input);
    }
}
