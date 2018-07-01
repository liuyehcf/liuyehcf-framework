package org.liuyehcf.compile.engine.hua.core.bytecode.ir;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 返回float
 * < before → after >
 * < value → [empty] >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _freturn extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xae;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        float value = context.popFloat();
        context.setReturnValue(value);
    }
}