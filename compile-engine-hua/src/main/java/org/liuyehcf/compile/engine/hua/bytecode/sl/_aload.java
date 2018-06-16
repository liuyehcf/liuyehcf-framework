package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _aload extends StoreLoad {

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
