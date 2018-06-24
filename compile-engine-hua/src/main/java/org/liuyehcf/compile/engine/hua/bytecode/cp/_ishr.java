package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 循环右移
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ishr extends Compute {

    public static final int OPERATOR_CODE = 0x7a;

    public _ishr() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
