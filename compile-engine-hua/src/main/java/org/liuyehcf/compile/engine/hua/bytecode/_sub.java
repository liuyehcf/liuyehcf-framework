package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class _sub implements ByteCode {
    private final int leftOffset;

    private final int rightOffset;

    private final int resultOffset;

    public _sub(int leftOffset, int rightOffset, int resultOffset) {
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.resultOffset = resultOffset;
    }

    @Override
    public void operate() {

    }
}
