package org.liuyehcf.compile.engine.hua.bytecode.sm;

/**
 * 赋值栈顶两个操作数
 * < before → after >
 * < {value2, value1} → {value2, value1}, {value2, value1} >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _dup2 extends OperatorStackManagement {

    public static final int OPERATOR_CODE = 0x5c;

    public _dup2() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
