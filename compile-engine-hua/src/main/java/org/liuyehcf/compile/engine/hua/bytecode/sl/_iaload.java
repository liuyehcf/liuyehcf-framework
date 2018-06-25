package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 数组元素加载
 * < before → after >
 * < arrayref, index → value >
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _iaload extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x2e;

    public _iaload() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
