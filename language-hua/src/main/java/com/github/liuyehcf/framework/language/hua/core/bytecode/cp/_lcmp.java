package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 比较两个long型变量的大小
 * 0：相同
 * 1：value1 > value2
 * -1：value1 < value2
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _lcmp extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x94;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        long value2 = context.popLong();
        long value1 = context.popLong();

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
