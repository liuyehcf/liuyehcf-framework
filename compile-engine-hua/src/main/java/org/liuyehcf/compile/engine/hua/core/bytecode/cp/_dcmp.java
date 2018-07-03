package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 比较两个double型变量的大小
 * 0：相同
 * 1：value1 > value2
 * -1：value1 < value2
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _dcmp extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x98;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        double value2 = context.popDouble();
        double value1 = context.popDouble();

        int result;
        if (value1 == value2) {
            result = 0;
        } else if (value1 > value2) {
            result = 1;
        } else {
            result = -1;
        }

        context.push(result);
        context.increaseCodeOffset();
    }
}

