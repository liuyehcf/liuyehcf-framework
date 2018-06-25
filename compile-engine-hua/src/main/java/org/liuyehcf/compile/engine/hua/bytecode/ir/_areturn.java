package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 返回对象类型
 * < before → after >
 * < objectref → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _areturn extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb0;

    public _areturn() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
