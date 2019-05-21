package com.github.liuyehcf.framework.language.hua.core.bytecode;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 字节码抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public abstract class ByteCode {

    public final String getName() {
        return getClass().getSimpleName();
    }

    public abstract void operate(RuntimeContext context);

    public abstract Object[] getOperators();

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
