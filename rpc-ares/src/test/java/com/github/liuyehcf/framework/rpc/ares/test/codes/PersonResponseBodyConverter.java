package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/10
 */
@Component
public class PersonResponseBodyConverter extends ResponseBodyConverter<Person> {

    @Override
    public Person convert(byte[] input, Type outputType) {
        return JSON.parseObject(input, Person.class);
    }

    @Override
    public int order() {
        return 0;
    }
}
