package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/10
 */
@Component
public class PersonRequestBodyConverter extends RequestBodyConverter<Person> {

    @Override
    public byte[] convert(Person input, Type outputType) {
        return JSON.toJSONBytes(input);
    }

    @Override
    public int order() {
        return 0;
    }
}
