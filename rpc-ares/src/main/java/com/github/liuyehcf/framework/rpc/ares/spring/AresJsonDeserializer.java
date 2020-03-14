package com.github.liuyehcf.framework.rpc.ares.spring;

import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2019/11/11
 */
@Data
@AllArgsConstructor
public class AresJsonDeserializer {

    private Type type;
    private JsonDeserializer<?> deserializer;
}
