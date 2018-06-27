package org.liuyehcf.compile.engine.hua.core.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

/**
 * 一维数组创建指令
 * < before → after >
 * < count → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _newarray extends ObjectCreate {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xbc;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class};

    /**
     * 类型
     */
    private final String type;

    public _newarray(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate(RuntimeContext context) {
        int count = context.pop();

        Type t = Type.parse(type);
        assertFalse(t.isArrayType());
        int width = t.getTypeWidth();

        int reference = HeapMemoryManagement.allocate(width, count);
        context.push(reference);

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{type};
    }
}
