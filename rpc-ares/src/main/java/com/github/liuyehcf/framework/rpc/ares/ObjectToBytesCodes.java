package com.github.liuyehcf.framework.rpc.ares;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public abstract class ObjectToBytesCodes<INPUT> extends Codes<INPUT, byte[]> {

    @Override
    public int order() {
        return 0;
    }

    @Override
    public abstract byte[] encode(INPUT obj);

    @Override
    public abstract INPUT decode(byte[] obj, Type expectedPlainType);
}
