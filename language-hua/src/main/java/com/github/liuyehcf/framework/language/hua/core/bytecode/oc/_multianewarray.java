package com.github.liuyehcf.framework.language.hua.core.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.runtime.HeapMemoryManagement;
import com.github.liuyehcf.framework.language.hua.runtime.Reference;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 多维数组创建指令，指定的维度有多个
 * < before → after >
 * < count1, [count2,...] → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _multianewarray extends ObjectCreate {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xc5;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class, int.class};

    /**
     * 类型
     */
    private final String type;

    /**
     * 维度表达式大小
     */
    private final int expressionDimSize;

    public _multianewarray(String type, int expressionDimSize) {
        this.type = type;
        this.expressionDimSize = expressionDimSize;
    }

    public String getType() {
        return type;
    }

    public int getExpressionDimSize() {
        return expressionDimSize;
    }

    @Override
    public void operate(RuntimeContext context) {
        int[] counts = new int[expressionDimSize];

        for (int i = expressionDimSize - 1; i >= 0; i--) {
            counts[i] = context.pop();
        }

        Type t = Type.parse(type);

        Reference reference = doCreate(counts, 0, t);
        context.push(reference);

        context.increaseCodeOffset();
    }

    private Reference doCreate(int[] counts, int index, Type t) {
        Reference reference = HeapMemoryManagement.allocate(t.getTypeWidth(), counts[index]);
        Type nextType = t.toDimDecreasedType();
        if (!nextType.isArrayType()) {
            return reference;
        }
        for (int i = 0; i < counts[index]; i++) {
            HeapMemoryManagement.storeReference(reference.getAddress() + i * t.getTypeWidth(), doCreate(counts, index + 1, t.toDimDecreasedType()));
        }
        return reference;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{type, expressionDimSize};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + type + ", " + expressionDimSize;
    }
}
