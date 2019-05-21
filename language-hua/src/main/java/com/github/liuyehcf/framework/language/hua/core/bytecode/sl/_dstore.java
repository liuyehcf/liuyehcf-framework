package com.github.liuyehcf.framework.language.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * double 存储
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _dstore extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x39;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 标志符序号
     */
    private final int order;

    public _dstore(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public void operate(RuntimeContext context) {
        double value = context.popDouble();

        context.storeDouble(order, value);

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{order};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + order;
    }
}

