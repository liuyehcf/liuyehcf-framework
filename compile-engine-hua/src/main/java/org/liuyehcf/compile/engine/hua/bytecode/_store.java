package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author chenlu
 * @date 2018/6/8
 */
public class _store implements ByteCode {

    private final int fromOffset;

    private final int toOffset;

    public _store(int fromOffset, int toOffset) {
        this.fromOffset = fromOffset;
        this.toOffset = toOffset;
    }

    public int getFromOffset() {
        return fromOffset;
    }

    public int getToOffset() {
        return toOffset;
    }

    @Override
    public void operate() {

    }
}
