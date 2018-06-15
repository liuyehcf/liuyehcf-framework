package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class _iconst extends StoreLoad {
    private final int value;

    public _iconst(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void operate() {

    }
}
