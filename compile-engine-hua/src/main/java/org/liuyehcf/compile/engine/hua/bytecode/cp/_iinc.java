package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 自增
 * < before → after >
 * <  →  >
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _iinc extends Compute {

    public static final int OPERATOR_CODE = 0x84;
    /**
     * 偏移量
     */
    private int offset;
    /**
     * 增量
     */
    private int increment;

    public _iinc() {
        super(OPERATOR_CODE, 2, new int[]{4, 4});
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    @Override
    public void operate() {

    }
}
