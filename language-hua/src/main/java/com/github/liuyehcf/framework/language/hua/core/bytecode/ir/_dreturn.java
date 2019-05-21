package com.github.liuyehcf.framework.language.hua.core.bytecode.ir;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 返回double
 * < before → after >
 * < value → [empty] >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _dreturn extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xaf;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        double value = context.popDouble();
        context.setReturnValue(value);
    }
}
