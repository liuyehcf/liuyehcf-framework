package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public abstract class ObjectToBytesCodes<INPUT> extends Codes<INPUT, byte[]> {

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public abstract byte[] encode(INPUT obj);

    @Override
    public abstract INPUT decode(byte[] obj, Type expectedPlainType);
}
