package org.liuyehcf.compile.engine.hua.bytecode.cf;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 跳转指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class ControlTransfer extends ByteCode {

    private static final int UNINITIALIZED = -1;

    /**
     * 代码偏移量
     */
    private int codeOffset = UNINITIALIZED;

    public int getCodeOffset() {
        return codeOffset;
    }

    public void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }
}
