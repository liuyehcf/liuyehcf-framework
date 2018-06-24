package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * int 加载
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _iload extends StoreLoad {

    public static final int OPERATOR_CODE = 0x15;
    /**
     * 偏移量
     */
    private final int offset;

    public _iload(int offset) {
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
