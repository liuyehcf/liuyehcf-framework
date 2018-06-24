package org.liuyehcf.compile.engine.hua.bytecode.cp;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 运算指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class Compute extends ByteCode {

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 0;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{};

    public Compute(int operatorCode) {
        super(operatorCode, OPERATOR_NUM, OPERATOR_CLASSES);
    }

    public Compute(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        super(operatorCode, operatorNum, operatorClasses);
    }
}
