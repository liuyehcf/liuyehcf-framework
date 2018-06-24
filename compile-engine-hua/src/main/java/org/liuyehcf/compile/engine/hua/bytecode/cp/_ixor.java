package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 位异或
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ixor extends Compute {

    public static final int OPERATOR_CODE = 0x82;

    public _ixor() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
