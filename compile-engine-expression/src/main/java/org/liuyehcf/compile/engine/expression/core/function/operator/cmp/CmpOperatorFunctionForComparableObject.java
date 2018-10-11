package org.liuyehcf.compile.engine.expression.core.function.operator.cmp;

import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2018/10/2
 */
public class CmpOperatorFunctionForComparableObject extends CmpOperatorFunction {
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() instanceof Comparable && arg2.getValue() instanceof Comparable
                && Objects.equals(arg1.getValue().getClass(), arg2.getValue().getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        return ExpressionValue.valueOf(((Comparable) arg1.getValue()).compareTo(arg2.getValue()));
    }
}
