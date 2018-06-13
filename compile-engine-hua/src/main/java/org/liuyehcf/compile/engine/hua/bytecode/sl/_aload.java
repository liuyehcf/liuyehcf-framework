package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public class _aload implements ByteCode {

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
