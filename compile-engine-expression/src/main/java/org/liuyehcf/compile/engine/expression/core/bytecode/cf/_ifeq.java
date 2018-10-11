package org.liuyehcf.compile.engine.expression.core.bytecode.cf;

import org.liuyehcf.compile.engine.expression.core.model.ComparableValue;

/**
 * 跳转指令，等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _ifeq extends ConditionalControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x99;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _ifeq() {
    }

    public _ifeq(int codeOffset) {
        super(codeOffset);
    }

    @Override
    boolean accept(ComparableValue comparableValue) {
        return comparableValue.getValue() == 0;
    }
}
