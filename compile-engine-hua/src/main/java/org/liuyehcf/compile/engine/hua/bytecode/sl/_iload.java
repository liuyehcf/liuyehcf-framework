package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * int 加载
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _iload extends StoreLoad {

    private final int offset;

    public _iload(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }

}
