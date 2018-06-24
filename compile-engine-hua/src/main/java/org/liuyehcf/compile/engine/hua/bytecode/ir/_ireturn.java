package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 返回整型
 * < before → after >
 * < value → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _ireturn extends InvokeAndReturn {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xac;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 0;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{};

    public _ireturn() {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
    }

    @Override
    public void operate() {

    }
}
