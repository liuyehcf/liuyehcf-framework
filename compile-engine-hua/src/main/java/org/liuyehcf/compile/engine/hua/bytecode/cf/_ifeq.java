package org.liuyehcf.compile.engine.hua.bytecode.cf;

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

    public _ifeq() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
