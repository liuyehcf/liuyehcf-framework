package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 加载数组元素，元素类型是对象类型
 * < before → after >
 * < arrayref, index → value >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _aaload extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x32;

    public _aaload() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
