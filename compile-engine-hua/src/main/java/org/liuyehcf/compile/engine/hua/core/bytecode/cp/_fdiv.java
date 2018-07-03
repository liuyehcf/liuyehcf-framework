package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * float 除法
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _fdiv extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x6e;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        float value2 = context.popFloat();
        float value1 = context.popFloat();

        context.push(value1 / value2);

        context.increaseCodeOffset();
    }
}
