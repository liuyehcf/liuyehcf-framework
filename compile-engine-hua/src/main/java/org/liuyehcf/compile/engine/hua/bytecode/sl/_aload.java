package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 加载对象（包括数组）
 * < before → after >
 * < → objectref >
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _aload extends StoreLoad {

    /**
     * 标志符偏移量
     */
    private final int offset;

    public _aload(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }
}
