package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 数组元素加载
 * < before → after >
 * < arrayref, index → value >
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _iaload extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x2e;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int index = context.pop();
        int arrayOffset = context.pop();

        int elementOffset = arrayOffset + index * Type.TYPE_INT.getTypeWidth();

        int value = HeapMemoryManagement.loadInt(elementOffset);

        context.push(value);

        context.increaseCodeOffset();
    }
}
