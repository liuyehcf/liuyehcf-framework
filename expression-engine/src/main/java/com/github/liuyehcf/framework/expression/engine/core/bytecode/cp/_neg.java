package com.github.liuyehcf.framework.expression.engine.core.bytecode.cp;

import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 取负数
 * < before → after >
 * < value → result >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _neg extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x77;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        operateForOneOperand(context, OperatorType.NEG);
    }

    @Override
    public int getStackOperandNum() {
        return 1;
    }
}
