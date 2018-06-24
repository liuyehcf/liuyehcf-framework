package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 乘法
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _imul extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x68;

    public _imul() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
