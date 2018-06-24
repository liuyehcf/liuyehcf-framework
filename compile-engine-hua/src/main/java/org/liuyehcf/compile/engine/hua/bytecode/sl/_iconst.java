package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 加载整型常量
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class _iconst extends StoreLoad {

    /**
     * 唯一操作码
     * todo 这个码待定
     */
    public static final int OPERATOR_CODE = 0x02;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 1;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 常量值
     */
    private final int value;

    public _iconst(int value) {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void operate() {

    }
}
