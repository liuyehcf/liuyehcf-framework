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

    public static final int OPERATOR_CODE = 0x36;
    /**
     * 偏移量
     */
    private final int offset;

    public _istore(int offset) {
        super(OPERATOR_CODE, 1, new int[]{4});
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }
}
