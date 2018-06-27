package org.liuyehcf.compile.engine.hua.core.bytecode.cf;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 跳转指令，大于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpgt extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa3;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _if_icmpgt() {
    }

    public _if_icmpgt(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        int value2 = context.pop();
        int value1 = context.pop();

        if (value1 > value2) {
            context.setCodeOffset(getCodeOffset());
        } else {
            context.increaseCodeOffset();
        }
    }
}