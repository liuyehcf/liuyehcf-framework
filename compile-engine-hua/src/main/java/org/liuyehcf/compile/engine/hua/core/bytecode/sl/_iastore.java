package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

/**
 * 数组元素存储
 * < before → after >
 * < arrayref, index, value → >
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _iastore extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x4f;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate() {

    }
}
