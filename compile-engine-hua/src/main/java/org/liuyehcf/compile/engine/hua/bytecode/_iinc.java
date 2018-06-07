package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class _iinc implements ByteCode {
    private final int increment;

    public _iinc(int increment) {
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }
}
