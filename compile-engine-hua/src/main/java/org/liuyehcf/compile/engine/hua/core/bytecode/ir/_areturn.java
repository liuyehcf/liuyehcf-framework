package org.liuyehcf.compile.engine.hua.core.bytecode.ir;

import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

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

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {

    }
}
