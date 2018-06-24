package org.liuyehcf.compile.engine.hua.bytecode.ir;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 方法调用与返回指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class InvokeAndReturn extends ByteCode {
    public InvokeAndReturn(int operatorCode, int operatorNum, int[] operatorByteSize) {
        super(operatorCode, operatorNum, operatorByteSize);
    }
}
