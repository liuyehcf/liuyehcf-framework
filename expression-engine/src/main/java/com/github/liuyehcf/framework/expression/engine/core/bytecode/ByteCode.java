package com.github.liuyehcf.framework.expression.engine.core.bytecode;

import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 字节码抽象基类
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public abstract class ByteCode {

    public final String getName() {
        return getClass().getSimpleName();
    }

    public abstract Object[] getOperators();

    public abstract void operate(RuntimeContext context);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
