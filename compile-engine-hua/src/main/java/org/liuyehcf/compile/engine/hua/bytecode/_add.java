package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class _add implements ByteCode {
    private final int leftOffset;

    private final int rightOffset;

    private final int resultOffset;

    public _add(int leftOffset, int rightOffset, int resultOffset) {
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.resultOffset = resultOffset;
    }

    @Override
    public void operate() {

    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public int getResultOffset() {
        return resultOffset;
    }
}
