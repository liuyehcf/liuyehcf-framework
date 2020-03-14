package com.github.liuyehcf.framework.rpc.ares.spring;

import com.google.gson.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2019/11/11
 */
@Data
@AllArgsConstructor
public class AresJsonSerializer {

    private Type type;
    private JsonSerializer<?> serializer;
}
