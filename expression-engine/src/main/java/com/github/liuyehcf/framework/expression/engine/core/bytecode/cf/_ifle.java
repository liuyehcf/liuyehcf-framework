package com.github.liuyehcf.framework.expression.engine.core.bytecode.cf;

import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;

/**
 * 小于等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _ifle extends ConditionalControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x9e;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _ifle() {
    }

    public _ifle(int codeOffset) {
        super(codeOffset);
    }

    @Override
    boolean accept(ComparableValue comparableValue) {
        return comparableValue.getValue() <= 0;
    }
}
