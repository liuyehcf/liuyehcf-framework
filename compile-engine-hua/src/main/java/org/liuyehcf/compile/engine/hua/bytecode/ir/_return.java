package org.liuyehcf.compile.engine.hua.bytecode.ir;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 返回类型为void的方法的返回指令
 * < before → after >
 * <  → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/17
 */
public class _return extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb1;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    static {
        ByteCode.register(OPERATOR_CODE, _return.class);
    }

    @Override
    public void operate() {

    }
}
