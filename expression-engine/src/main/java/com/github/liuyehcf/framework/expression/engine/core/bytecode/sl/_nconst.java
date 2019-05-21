package com.github.liuyehcf.framework.expression.engine.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 加载null常量
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/9/30
 */
public class _nconst extends Const {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x06;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    public _nconst() {
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return OPERATORS;
    }

    @Override
    public void operate(RuntimeContext context) {
        context.push(ExpressionValue.valueOf(null));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
