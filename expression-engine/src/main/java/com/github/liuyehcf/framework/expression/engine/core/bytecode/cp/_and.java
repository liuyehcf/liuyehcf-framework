package com.github.liuyehcf.framework.expression.engine.core.bytecode.cp;

import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 位与
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _and extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x7e;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        operateForTwoOperand(context, OperatorType.BIT_AND);
    }

    @Override
    public int getStackOperandNum() {
        return 2;
    }
}
