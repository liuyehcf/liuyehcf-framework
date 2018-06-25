package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 跳转指令，等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class _ifeq extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x99;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    static {
        ByteCode.register(OPERATOR_CODE, _ifeq.class);
    }

    @Override
    public void operate() {

    }
}
