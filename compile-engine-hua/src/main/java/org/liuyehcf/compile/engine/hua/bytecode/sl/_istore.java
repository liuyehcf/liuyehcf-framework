package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * int 存储
 *
 * @author hechenfeng
 * @date 2018/6/8
 */
public class _istore extends StoreLoad {

    /**
     * 偏移量
     */
    private final int offset;

    public _istore(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }
}
