package org.liuyehcf.compile.engine.expression.core.function.operator.cmp;

import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class CmpOperatorFunctionForDouble extends CmpOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return compatibleDouble(arg1, arg2);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Number value1 = arg1.getValue();
        Number value2 = arg2.getValue();

        return ExpressionValue.valueOf(Double.compare(value1.doubleValue(), value2.doubleValue()));
    }
}
