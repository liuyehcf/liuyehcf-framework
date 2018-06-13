package org.liuyehcf.compile.engine.hua.bytecode.cp;

/**
 * int 自增
 *
 * @author chenlu
 * @date 2018/6/6
 */
public class _iinc extends Compute {


    private final int increment;

    public _iinc(int increment) {
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }

    @Override
    public void operate() {

    }

}
