package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，小于等于时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmple extends ControlTransfer {

    public static final int OPERATOR_CODE = 0xa4;

    public _if_icmple() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}