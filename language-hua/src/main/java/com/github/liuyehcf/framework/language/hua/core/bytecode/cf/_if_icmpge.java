package com.github.liuyehcf.framework.language.hua.core.bytecode.cf;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 跳转指令，大于等于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpge extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa2;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _if_icmpge() {
    }

    public _if_icmpge(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        int value2 = context.pop();
        int value1 = context.pop();

        if (value1 >= value2) {
            context.setCodeOffset(getCodeOffset());
        } else {
            context.increaseCodeOffset();
        }
    }
}