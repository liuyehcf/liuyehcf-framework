package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * int 存储
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/8
 */
public class _istore extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x36;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 1;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 偏移量
     */
    private final int offset;

    public _istore(int offset) {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }

    @Override
    public Object[] getOperators() {
        return new Object[]{offset};
    }
}
