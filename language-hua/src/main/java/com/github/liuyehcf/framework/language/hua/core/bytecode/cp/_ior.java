package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * int 位或
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ior extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x80;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int value2 = context.pop();
        int value1 = context.pop();

        context.push(value1 | value2);

        context.increaseCodeOffset();
    }
}
