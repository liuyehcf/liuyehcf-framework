package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;
import sun.rmi.rmic.iiop.Type;

/**
 * 加载常量
 * < before → after >
 * → value
 *
 * @author hechenfeng
 * @date 2018/6/27
 */
public class _ldc extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x12;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 常量池偏移量
     */
    private final int constantPoolOffset;

    public _ldc(int constantPoolOffset) {
        this.constantPoolOffset = constantPoolOffset;
    }

    @Override
    public void operate(RuntimeContext context) {
        String constant = context.getConstant(constantPoolOffset);

        int reference = HeapMemoryManagement.allocate(Type.TYPE_CHAR, constant.length());

        context.push(reference);
        
        context.increaseCodeOffset();
    }

    public int getConstantPoolOffset() {
        return constantPoolOffset;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{constantPoolOffset};
    }
}
