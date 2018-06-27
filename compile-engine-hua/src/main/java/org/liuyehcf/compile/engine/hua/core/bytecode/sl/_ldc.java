package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

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

    }

    @Override
    public Object[] getOperators() {
        return new Object[]{constantPoolOffset};
    }
}
