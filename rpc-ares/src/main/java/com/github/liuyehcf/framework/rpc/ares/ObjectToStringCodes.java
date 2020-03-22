package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public abstract class ObjectToStringCodes<INPUT> extends Codes<INPUT, String> {

    @Override
    public int order() {
        return 0;
    }

    @Override
    public abstract String encode(INPUT obj);

    @Override
    public abstract INPUT decode(String obj, Type expectedPlainType);
}
