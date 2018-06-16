package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class ControlTransfer implements ByteCode {

    private static final int UNINITIALIZED = -1;

    private int codeOffset = UNINITIALIZED;

    public int getCodeOffset() {
        return codeOffset;
    }

    public void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }
}
