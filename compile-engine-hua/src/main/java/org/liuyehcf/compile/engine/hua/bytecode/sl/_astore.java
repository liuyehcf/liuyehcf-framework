package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 存储对象（包括数组）
 * < before → after >
 * < objectref → >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _astore extends StoreLoad {

    /**
     * 标志符偏移量
     */
    private final int offset;

    public _astore(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }
}
