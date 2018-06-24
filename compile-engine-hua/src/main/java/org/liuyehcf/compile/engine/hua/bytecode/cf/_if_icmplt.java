package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，小于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmplt extends ControlTransfer {

    public static final int OPERATOR_CODE = 0xa1;

    public _if_icmplt() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}