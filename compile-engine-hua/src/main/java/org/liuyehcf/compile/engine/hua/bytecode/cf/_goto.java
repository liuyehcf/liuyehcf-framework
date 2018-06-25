package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 跳转指令，无条件跳转
 * < before → after >
 * < → >
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _goto extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa7;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    static {
        ByteCode.register(OPERATOR_CODE, _goto.class);
    }

    @Override
    public void operate() {

    }
}
