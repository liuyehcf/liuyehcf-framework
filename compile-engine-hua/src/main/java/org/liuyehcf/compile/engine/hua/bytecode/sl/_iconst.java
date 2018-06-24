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

    // todo 这个码待定
    public static final int OPERATOR_CODE = 0x02;
    /**
     * 常量值
     */
    private final int value;

    public _iconst(int value) {
        super(OPERATOR_CODE, 1, new int[]{4});
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void operate() {

    }
}
