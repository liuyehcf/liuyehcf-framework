package org.liuyehcf.compile.engine.expression.core.function;

import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public abstract class Function {

    /**
     * 函数名称
     */
    public abstract String getName();

    /**
     * 零元函数
     */
    public ExpressionValue call() {
        throw new ExpressionException(getName() + "() hasn't been defined");
    }

    /**
     * 一元函数
     */
    public ExpressionValue call(ExpressionValue arg) {
        throw new ExpressionException(getName() + "(arg) hasn't been defined");
    }

    /**
     * 二元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        throw new ExpressionException(getName() + "(arg1, arg2) hasn't been defined");
    }

    /**
     * 三元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3) hasn't been defined");
    }

    /**
     * 四元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4) hasn't been defined");
    }

    /**
     * 五元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5) hasn't been defined");
    }

    /**
     * 六元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6) hasn't been defined");
    }

    /**
     * 七元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6, arg7) hasn't been defined");
    }

    /**
     * 八元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) hasn't been defined");
    }

    /**
     * 九元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) hasn't been defined");
    }

    /**
     * 十元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) hasn't been defined");
    }

    /**
     * 多元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10, ExpressionValue... args) {
        throw new ExpressionException(getName() + "(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, args) hasn't been defined");
    }

    protected final ExpressionException createTypeIllegalException(int argIndex, Object value) {
        return new ExpressionException(getName() + "'s arg" + argIndex + " is incompatible with type '" + (value == null ? null : value.getClass().getName()) + "'");
    }
}
