package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * int取负数
 * < before → after >
 * < value → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _ineg extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x74;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();
        context.push(-value);
        context.increaseCodeOffset();
    }
}
