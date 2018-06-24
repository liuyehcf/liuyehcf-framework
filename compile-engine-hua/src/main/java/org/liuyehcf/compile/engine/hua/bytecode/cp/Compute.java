package org.liuyehcf.compile.engine.hua.bytecode.cp;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 运算指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class Compute extends ByteCode {
    public Compute(int operatorCode) {
        super(operatorCode, 0, null);
    }

    public Compute(int operatorCode, int operatorNum, int[] operatorByteSize) {
        super(operatorCode, operatorNum, operatorByteSize);
    }
}
