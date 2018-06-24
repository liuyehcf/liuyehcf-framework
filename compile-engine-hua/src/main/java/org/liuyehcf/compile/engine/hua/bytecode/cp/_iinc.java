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

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x84;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 2;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class, int.class};

    /**
     * 偏移量
     */
    private int offset;

    /**
     * 增量
     */
    private int increment;

    public _iinc() {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
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
