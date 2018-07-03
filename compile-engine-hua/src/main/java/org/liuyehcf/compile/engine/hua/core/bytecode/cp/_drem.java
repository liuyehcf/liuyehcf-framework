package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * double 求余
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _drem extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x73;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        double value2 = context.popDouble();
        double value1 = context.popDouble();

        context.push(value1 % value2);

        context.increaseCodeOffset();
    }
}
