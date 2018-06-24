package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 返回类型为void的方法的返回指令
 * < before → after >
 * <  → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/17
 */
public class _return extends InvokeAndReturn {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb1;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 0;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{};

    public _return() {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
    }

    @Override
    public void operate() {

    }
}
