package org.liuyehcf.compile.engine.expression.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.runtime.RuntimeContext;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * 加载数组元素
 * < before → after >
 * < array, index → item >
 *
 * @author hechenfeng
 * @date 2018/9/26
 */
public class _aaload extends Load {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x32;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return OPERATORS;
    }

    @Override
    public void operate(RuntimeContext context) {
        Object index = context.pop().getValue();

        Object array = context.pop().getValue();
        Object item;

        if (array instanceof List) {
            item = ((List) array).get((int) (long) index);
        } else if (array instanceof Map) {
            item = ((Map) array).get(index);
        } else if (array.getClass().isArray()) {
            item = Array.get(array, (int) (long) index);
        } else {
            throw new ExpressionException("operator '[]' cannot apply to non array type");
        }

        context.push(ExpressionValue.valueOf(item));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

