package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 跳转指令，不等时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpne extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa0;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    static {
        ByteCode.register(OPERATOR_CODE, _if_icmpne.class);
    }

    @Override
    public void operate() {

    }
}