package com.github.liuyehcf.framework.expression.engine.core.bytecode.cf;

import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 跳转指令，无条件跳转
 * < before → after >
 * < → >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _goto extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa7;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _goto() {
    }

    public _goto(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        context.setCodeOffset(getCodeOffset());
    }
}
