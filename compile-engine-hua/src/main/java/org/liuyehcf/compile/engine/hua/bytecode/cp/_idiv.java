package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 除法
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _idiv extends Compute {

    public static final int OPERATOR_CODE = 0x6c;

    public _idiv() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
