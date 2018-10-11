package org.liuyehcf.compile.engine.expression.core.bytecode.cp;

import org.liuyehcf.compile.engine.expression.core.model.OperatorType;
import org.liuyehcf.compile.engine.expression.runtime.RuntimeContext;

/**
 * 无符号右移
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _ushr extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x7c;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        operateForTwoOperand(context, OperatorType.USHR);
    }

    @Override
    public int getStackOperandNum() {
        return 2;
    }
}
