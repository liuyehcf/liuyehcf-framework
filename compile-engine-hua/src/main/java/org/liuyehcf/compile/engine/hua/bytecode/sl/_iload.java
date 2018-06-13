package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * int 加载
 *
 * @author chenlu
 * @date 2018/6/10
 */
public class _iload implements ByteCode {

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
