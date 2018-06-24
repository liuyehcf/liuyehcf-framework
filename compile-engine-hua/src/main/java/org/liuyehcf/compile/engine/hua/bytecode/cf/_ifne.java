package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，不等于0时跳转
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class _ifne extends ControlTransfer {

    public static final int OPERATOR_CODE = 0x9a;

    public _ifne() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}