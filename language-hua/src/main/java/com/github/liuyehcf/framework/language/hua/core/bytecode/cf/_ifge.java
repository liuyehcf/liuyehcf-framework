package com.github.liuyehcf.framework.language.hua.core.bytecode.cf;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 大于等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _ifge extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x9c;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    public _ifge() {
    }

    public _ifge(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();

        if (value >= 0) {
            context.setCodeOffset(getCodeOffset());
        } else {
            context.increaseCodeOffset();
        }
    }
}
