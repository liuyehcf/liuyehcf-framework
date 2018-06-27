package org.liuyehcf.compile.engine.hua.core.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

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

        int reference = doCreate(counts, 0, t);
        context.push(reference);

        context.increaseCodeOffset();
    }

    private int doCreate(int[] counts, int index, Type t) {
        int reference = HeapMemoryManagement.allocate(t.getTypeWidth(), counts[index]);
        Type nextType = t.toDimDecreasedType();
        if (!nextType.isArrayType()) {
            return reference;
        }
        for (int i = 0; i < counts[index]; i++) {
            HeapMemoryManagement.storeReference(reference + i * t.getTypeWidth(), doCreate(counts, index + 1, t.toDimDecreasedType()));
        }
        return reference;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{type, expressionDimSize};
    }
}
