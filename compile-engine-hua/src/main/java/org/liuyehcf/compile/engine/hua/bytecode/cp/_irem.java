package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 求余
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _irem extends Compute {

    public static final int OPERATOR_CODE = 0x70;

    public _irem() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
