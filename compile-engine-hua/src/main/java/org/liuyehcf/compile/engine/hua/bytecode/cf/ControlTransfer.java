package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public abstract class ControlTransfer implements ByteCode {
    private final int codeOffset;

    public ControlTransfer(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    public int getCodeOffset() {
        return codeOffset;
    }
}
