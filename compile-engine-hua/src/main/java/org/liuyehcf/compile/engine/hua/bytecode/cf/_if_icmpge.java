package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，大于等于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpge extends ControlTransfer {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xa2;

    public _if_icmpge() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}