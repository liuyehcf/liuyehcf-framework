package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToStringCodes;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/21
 */
@Component
public class PersonToStringCodesError extends ObjectToStringCodes<Person> {

    @Override
    public String encode(Person obj) {
        throw new RuntimeException();
    }

    @Override
    public Person decode(String obj, Type expectedPlainType) {
        throw new RuntimeException();
    }
}
