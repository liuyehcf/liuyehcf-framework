package org.liuyehcf.compile.engine.hua.bytecode.sm;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 赋值栈顶两个操作数
 * < before → after >
 * < {value2, value1} → {value2, value1}, {value2, value1} >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _dup2 extends OperatorStackManagement {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x5c;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    static {
        ByteCode.register(OPERATOR_CODE, _dup2.class);
    }

    @Override
    public void operate() {

    }
}
