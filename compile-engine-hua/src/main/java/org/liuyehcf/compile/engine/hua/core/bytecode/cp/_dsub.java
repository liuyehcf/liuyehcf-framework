package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * double 相减
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _dsub extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x67;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        double value2 = context.pop();
        double value1 = context.pop();

        context.push(value1 - value2);

        context.increaseCodeOffset();
    }
}