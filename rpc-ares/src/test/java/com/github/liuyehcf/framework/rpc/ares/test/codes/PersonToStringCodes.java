package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.ObjectToStringCodes;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
@Component
public class PersonToStringCodes extends ObjectToStringCodes<Person> {

    @Override
    public String encode(Person obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Person decode(String obj, Type expectedPlainType) {
        return JSON.parseObject(obj, expectedPlainType);
    }
}
