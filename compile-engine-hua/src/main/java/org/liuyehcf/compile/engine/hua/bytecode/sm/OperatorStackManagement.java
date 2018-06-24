package org.liuyehcf.compile.engine.hua.bytecode.sm;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 操作数栈管理指令基类
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public abstract class OperatorStackManagement extends ByteCode {
    public OperatorStackManagement(int operatorCode) {
        super(operatorCode, 0, null);
    }
}
