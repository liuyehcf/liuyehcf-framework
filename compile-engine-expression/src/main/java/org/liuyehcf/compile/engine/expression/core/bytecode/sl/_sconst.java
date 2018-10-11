package org.liuyehcf.compile.engine.expression.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.runtime.RuntimeContext;

/**
 * 加载String常量
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/9/30
 */
public class _sconst extends Const {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x07;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class};

    /**
     * 常量值
     */
    private final String value;

    public _sconst(String value) {
        this.value = value;
    }

    public String getValue() {
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

