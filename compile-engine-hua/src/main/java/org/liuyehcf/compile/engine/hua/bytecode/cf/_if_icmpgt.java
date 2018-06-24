package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，大于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpgt extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa3;

    public _if_icmpgt() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}