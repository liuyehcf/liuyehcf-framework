package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 存储数组元素，元素类型是对象类型
 * < before → after >
 * < arrayref, index, value → >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _aastore extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x53;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();
        int index = context.pop();
        int arrayReference = context.pop();

        int elementReference = arrayReference + index * Type.REFERENCE_TYPE_WIDTH;

        HeapMemoryManagement.storeInt(elementReference, value);

        context.increaseCodeOffset();
    }
}