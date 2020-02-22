package com.github.liuyehcf.framework.language.hua.core.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.runtime.HeapMemoryManagement;
import com.github.liuyehcf.framework.language.hua.runtime.Reference;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;

/**
 * 多维数组创建指令，指定的维度是第一维
 * < before → after >
 * < count → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _anewarray extends ObjectCreate {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xbd;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class};

    /**
     * 类型
     */
    private final String type;

    public _anewarray(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate(RuntimeContext context) {
        int count = context.pop();

        Type t = Type.parse(type);
        assertTrue(t.isArrayType(), "[SYSTEM_ERROR] - ByteCode cannot support non-array type");
        int width = t.getTypeWidth();

        Reference reference = HeapMemoryManagement.allocate(width, count);
        context.push(reference);

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{type};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + type;
    }
}
