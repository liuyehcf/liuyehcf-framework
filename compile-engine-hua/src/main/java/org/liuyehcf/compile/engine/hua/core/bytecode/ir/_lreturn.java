package org.liuyehcf.compile.engine.hua.core.bytecode.ir;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 返回long
 * < before → after >
 * < value → [empty] >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _lreturn extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xad;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        long value = context.popLong();
        context.setReturnValue(value);
    }
}

