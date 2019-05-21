package com.github.liuyehcf.framework.expression.engine.core.bytecode.ir;

import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 返回类型为void的方法的返回指令
 * < before → after >
 * <  → [empty] >
 *
 * @author hechenfeng
 * @date 2018/9/30
 */
public class _return extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb1;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
