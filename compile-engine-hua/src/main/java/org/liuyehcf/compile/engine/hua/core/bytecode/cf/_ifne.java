package org.liuyehcf.compile.engine.hua.core.bytecode.cf;

import org.liuyehcf.compile.engine.hua.runtime.OperatorStack;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 跳转指令，不等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class _ifne extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x9a;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _ifne() {
    }

    public _ifne(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        OperatorStack operatorStack = context.getOperatorStack();

        int value = operatorStack.pop();

        if (value != 0) {
            context.getCurrentMethod().setCodeOffset(getCodeOffset());
        } else {
            context.getCurrentMethod().increaseCodeOffset();
        }
    }
}