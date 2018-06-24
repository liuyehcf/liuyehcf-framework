package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 存储加载指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class StoreLoad extends ByteCode {

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{};

    public StoreLoad(int operatorCode) {
        super(operatorCode, 0, OPERATOR_CLASSES);
    }

    public StoreLoad(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        super(operatorCode, operatorNum, operatorClasses);
    }
}
