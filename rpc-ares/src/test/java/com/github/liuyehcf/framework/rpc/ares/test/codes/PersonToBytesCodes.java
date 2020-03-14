package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
@Component
public class PersonToBytesCodes extends ObjectToBytesCodes<Person> {

    @Override
    public byte[] encode(Person obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Person decode(byte[] obj, Type expectedPlainType) {
        return JSON.parseObject(obj, Person.class);
    }
}