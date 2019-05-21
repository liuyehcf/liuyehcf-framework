package com.github.liuyehcf.framework.expression.engine.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class _bconst extends Const {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x05;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{boolean.class};

    /**
     * 常量值
     */
    private final boolean value;

    public _bconst(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @JSONField(serialize = false)
    public ComparableValue getComparableValue() {
        return ComparableValue.valueOf(value);
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
