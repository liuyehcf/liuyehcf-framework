package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * float 取负数
 * < before → after >
 * < value → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _fneg extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x76;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        float value = context.popFloat();
        context.push(-value);
        context.increaseCodeOffset();
    }
}

