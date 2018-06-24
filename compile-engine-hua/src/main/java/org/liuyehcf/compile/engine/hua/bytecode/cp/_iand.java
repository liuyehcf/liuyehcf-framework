package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 位与
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _iand extends Compute {

    public static final int OPERATOR_CODE = 0x7e;

    public _iand() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
