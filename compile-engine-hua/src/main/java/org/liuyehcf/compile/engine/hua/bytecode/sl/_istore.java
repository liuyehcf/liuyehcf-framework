package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * int 存储
 * 此字节码的语义与Java的同名字节码不同
 * Java中，istore存储目标位于操作数中
 * Hua中，istore存储目标位于操作数栈中，因为这么实现比较简单
 *
 * @author chenlu
 * @date 2018/6/8
 */
public class _istore implements ByteCode {

    private final int offset;

    public _istore(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }

}
