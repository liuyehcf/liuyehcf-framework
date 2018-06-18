package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 自增
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _iinc extends Compute {

    private final int offset;

    private final int increment;

    public _iinc(int offset, int increment) {
        this.offset = offset;
        this.increment = increment;
    }

    public int getOffset() {
        return offset;
    }

    public int getIncrement() {
        return increment;
    }

    @Override
    public void operate() {

    }

}
