package org.liuyehcf.compile.engine.hua.bytecode.sm;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 操作数栈管理指令基类
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public abstract class OperatorStackManagement extends ByteCode {

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 0;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{};

    public OperatorStackManagement(int operatorCode) {
        super(operatorCode, OPERATOR_NUM, OPERATOR_CLASSES);
    }
}
