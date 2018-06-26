package org.liuyehcf.compile.engine.hua.core.bytecode.cf;

import org.liuyehcf.compile.engine.hua.runtime.OperatorStack;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 跳转指令，小于等于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmple extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa4;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _if_icmple() {
    }

    public _if_icmple(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        OperatorStack operatorStack = context.getOperatorStack();

        int value2 = operatorStack.pop();
        int value1 = operatorStack.pop();

        if (value1 <= value2) {
            context.getCurrentMethod().setCodeOffset(getCodeOffset());
        } else {
            context.getCurrentMethod().increaseCodeOffset();
        }
    }
}