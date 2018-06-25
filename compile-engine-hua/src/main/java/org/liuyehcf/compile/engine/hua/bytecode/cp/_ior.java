package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 位或
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ior extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x80;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate() {

    }
}
