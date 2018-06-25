package org.liuyehcf.compile.engine.hua.bytecode.cp;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * int 相加
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class _iadd extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x60;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    static {
        ByteCode.register(OPERATOR_CODE, _iadd.class);
    }

    @Override
    public void operate() {

    }
}
