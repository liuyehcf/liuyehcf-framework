package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 位或
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ior extends Compute {

    public static final int OPERATOR_CODE = 0x80;

    public _ior() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
