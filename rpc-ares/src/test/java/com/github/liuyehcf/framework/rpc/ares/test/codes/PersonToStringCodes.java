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
    public int order() {
        return 0;
    }

    @Override
    public String encode(Person plainObj) {
        return JSON.toJSONString(plainObj);
    }

    @Override
    public Person decode(String cipherObj, Type plainType) {
        return JSON.parseObject(cipherObj, plainType);
    }
}
