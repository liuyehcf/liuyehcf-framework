package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 相减
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class _isub extends Compute {

    public static final int OPERATOR_CODE = 0x64;

    public _isub() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
