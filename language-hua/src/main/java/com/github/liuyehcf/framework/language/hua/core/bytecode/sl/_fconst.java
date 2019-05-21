package com.github.liuyehcf.framework.language.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 加载float常量
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _fconst extends StoreLoad {

    /**
     * 唯一操作码(与Java有区别)
     */
    public static final int OPERATOR_CODE = 0x0b;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{float.class};

    /**
     * 常量值
     */
    private final float value;

    public _fconst(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void operate(RuntimeContext context) {
        context.push(this.value);

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{value};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + value;
    }
}
