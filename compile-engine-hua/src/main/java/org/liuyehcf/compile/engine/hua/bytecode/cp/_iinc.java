package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 自增
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _iinc extends Compute {

    private int offset;

    private int increment;


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
