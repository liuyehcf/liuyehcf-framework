package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.Reference;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 加载long型数组元素
 * < before → after >
 * < arrayref, index → value >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _laload extends ArrayStoreLoad {
    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x2f;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int index = context.pop();
        Reference arrayReference = context.pop();

        int elementOffset = arrayReference.getAddress() + index * Type.LONG_TYPE_WIDTH;

        long value = HeapMemoryManagement.loadLong(elementOffset);

        context.push(value);

        context.increaseCodeOffset();
    }
}
