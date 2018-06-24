package org.liuyehcf.compile.engine.hua.bytecode.cf;

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

    public _goto() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
