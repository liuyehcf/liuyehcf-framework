package org.liuyehcf.compile.engine.expression.core.function.operator.cmp;

import org.liuyehcf.compile.engine.expression.core.function.OperatorFunction;
import org.liuyehcf.compile.engine.expression.core.model.OperatorType;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public abstract class CmpOperatorFunction extends OperatorFunction {
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE >> 1;
    }

    @Override
    public final OperatorType getType() {
        return OperatorType.CMP;
    }

    @Override
    public final boolean accept(ExpressionValue arg) {
        return super.accept(arg);
    }

    @Override
    public final ExpressionValue call(ExpressionValue arg) {
        return super.call(arg);
    }
}
