package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.Reference;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 计算数组大小
 * < before → after >
 * < arrayref → value >
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
public class _sizeof extends Compute {
    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xcb;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 标志符序号
     */
    private final int order;

    public _sizeof(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public void operate(RuntimeContext context) {
        Reference reference = context.loadReference(order);

        context.push(reference.getSize());

        context.increaseCodeOffset();
    }

    @Override
    public Object[] getOperators() {
        return new Object[]{order};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + order;
    }
}
