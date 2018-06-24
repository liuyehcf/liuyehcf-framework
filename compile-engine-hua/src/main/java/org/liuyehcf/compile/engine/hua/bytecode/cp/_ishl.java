package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 循环左移
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _ishl extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x78;

    public _ishl() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
