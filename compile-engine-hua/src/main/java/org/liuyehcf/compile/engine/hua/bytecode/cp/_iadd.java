package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 相加
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class _iadd extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x60;

    public _iadd() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
