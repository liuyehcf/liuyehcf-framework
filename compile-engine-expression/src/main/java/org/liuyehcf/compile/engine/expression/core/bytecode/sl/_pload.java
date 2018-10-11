package org.liuyehcf.compile.engine.expression.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.runtime.RuntimeContext;

/**
 * 加载属性值
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _pload extends Load {
    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x19;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class};

    /**
     * 属性名
     */
    private final String propertyName;

    public _pload(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{propertyName};
    }

    @Override
    public void operate(RuntimeContext context) {
        Object property = context.getProperty(propertyName);
        context.push(ExpressionValue.valueOf(property));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getPropertyName();
    }
}
