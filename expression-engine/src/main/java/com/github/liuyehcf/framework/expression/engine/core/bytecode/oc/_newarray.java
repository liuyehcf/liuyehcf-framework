package com.github.liuyehcf.framework.expression.engine.core.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 一维数组创建指令
 * < before → after >
 * < item1,item2,... → array >
 *
 * @author hechenfeng
 * @date 2018/9/28
 */
public class _newarray extends ObjectCreate {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xbc;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 数组长度
     */
    private final int arraySize;

    public _newarray(int arraySize) {
        this.arraySize = arraySize;
    }

    public int getArraySize() {
        return arraySize;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{arraySize};
    }

    @Override
    public void operate(RuntimeContext context) {
        Object[] array = new Object[arraySize];

        int size = arraySize;
        while (size > 0) {
            size--;
            array[size] = context.pop().getValue();
        }

        context.push(ExpressionValue.valueOf(array));

        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getArraySize();
    }
}
