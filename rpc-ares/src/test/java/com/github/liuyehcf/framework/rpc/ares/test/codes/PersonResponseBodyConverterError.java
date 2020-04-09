package com.github.liuyehcf.framework.rpc.ares.test.codes;

import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/10
 */
@Component
public class PersonResponseBodyConverterError extends ResponseBodyConverter<Person> {

    @Override
    public Person convert(byte[] input, Type outputType) {
        throw new RuntimeException();
    }

    @Override
    public int order() {
        return -1;
    }
}
