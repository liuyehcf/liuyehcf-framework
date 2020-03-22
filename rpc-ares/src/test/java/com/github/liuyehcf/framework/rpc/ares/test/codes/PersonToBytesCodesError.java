package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/21
 */
@Component
public class PersonToBytesCodesError extends ObjectToBytesCodes<Person> {

    @Override
    public byte[] encode(Person plainObj) {
        throw new RuntimeException();
    }

    @Override
    public Person decode(byte[] cipherObj, Type plainType) {
        throw new RuntimeException();
    }
}
