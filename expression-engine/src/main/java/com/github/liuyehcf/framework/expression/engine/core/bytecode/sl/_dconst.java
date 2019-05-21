package com.github.liuyehcf.framework.expression.engine.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 加载double常量
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/9/30
 */
public class _dconst extends Const {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x04;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{double.class};

    /**
     * 常量值
     */
    private final double value;

    public _dconst(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{value};
    }

    @Override
    public void operate(RuntimeContext context) {
        context.push(ExpressionValue.valueOf(this.value));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getValue();
    }
}
