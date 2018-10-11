package org.liuyehcf.compile.engine.expression.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.core.model.ComparableValue;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.runtime.RuntimeContext;

/**
 * @author hechenfeng
 * @date 2018/10/2
 */
public class _cconst extends Const {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x08;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 常量值
     */
    private final int value;

    public _cconst(int value) {
        this.value = value;
    }

    public int getValue() {
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
        context.push(ExpressionValue.valueOf(getComparableValue()));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getValue();
    }
}
