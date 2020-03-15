package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 比较两个float型变量的大小
 * 0：相同
 * 1：value1 > value2
 * -1：value1 < value2
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _fcmp extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x96;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        float value2 = context.popFloat();
        float value1 = context.popFloat();

        int result;
        result = Float.compare(value1, value2);

        context.push(result);
        context.increaseCodeOffset();
    }
}

