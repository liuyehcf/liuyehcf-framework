package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.Reference;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 数组元素存储
 * < before → after >
 * arrayref, index, value →
 *
 * @author hechenfeng
 * @date 2018/6/27
 */
public class _bastore extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x54;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();
        int index = context.pop();
        Reference arrayReference = context.pop();

        int elementOffset = arrayReference.getAddress() + index * Type.BOOLEAN_TYPE_WIDTH;

        HeapMemoryManagement.storeBoolean(elementOffset, value);

        context.increaseCodeOffset();
    }
}
