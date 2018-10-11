package org.liuyehcf.compile.engine.expression.core.function;

import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.core.model.OperatorType;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public abstract class OperatorFunction extends Function implements Comparable<OperatorFunction> {

    private static final int ORDER_DEFAULT = 0;

    protected static boolean isLong(ExpressionValue arg) {
        return arg.getValue() instanceof Long;
    }

    protected static boolean bothLong(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() instanceof Long && arg2.getValue() instanceof Long;
    }

    protected static boolean isDouble(ExpressionValue arg) {
        return arg.getValue() instanceof Double;
    }

    protected static boolean compatibleDouble(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() instanceof Double && (arg2.getValue() instanceof Long || arg2.getValue() instanceof Double)
                || arg2.getValue() instanceof Double && (arg1.getValue() instanceof Long || arg1.getValue() instanceof Double);
    }

    protected static boolean anyString(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() instanceof String || arg2.getValue() instanceof String;
    }

    protected static boolean bothString(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() instanceof String && arg2.getValue() instanceof String;
    }

    @Override
    public final String getName() {
        return "operator";
    }

    public abstract OperatorType getType();

    public int getOrder() {
        return ORDER_DEFAULT;
    }

    /**
     * 零元函数
     */
    public final ExpressionValue call() {
        throw new ExpressionException("call() cannot be used as operator function");
    }

    /**
     * 类型匹配
     */
    public boolean accept(ExpressionValue arg) {
        throw new ExpressionException("accept(arg) cannot apply to operator[" + getType().getSymbol() + "]");
    }

    /**
     * 类型匹配
     */
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        throw new ExpressionException("accept(arg1, arg2) cannot apply to operator[" + getType().getSymbol() + "]");
    }

    /**
     * 一元函数
     */
    public ExpressionValue call(ExpressionValue arg) {
        throw new ExpressionException("operator[" + getType().getSymbol() + "](arg) is illegal");
    }

    /**
     * 二元函数
     */
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        throw new ExpressionException("operator[" + getType().getSymbol() + "](arg1, arg2) is illegal");
    }

    /**
     * 三元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        throw new ExpressionException("call(arg1, arg2, arg3) cannot be used as operator function");
    }

    /**
     * 四元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4) cannot be used as operator function");
    }

    /**
     * 五元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5) cannot be used as operator function");
    }

    /**
     * 六元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6) cannot be used as operator function");
    }

    /**
     * 七元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6, arg7) cannot be used as operator function");
    }

    /**
     * 八元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) cannot be used as operator function");
    }

    /**
     * 九元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) cannot be used as operator function");
    }

    /**
     * 十元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) cannot be used as operator function");
    }

    /**
     * 多元函数
     */
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10, ExpressionValue... args) {
        throw new ExpressionException("call(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, args) cannot be used as operator function");
    }

    @Override
    public final int compareTo(OperatorFunction o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }
}
