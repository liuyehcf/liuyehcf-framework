package org.liuyehcf.compile.engine.expression.core.function;

import org.liuyehcf.compile.engine.core.utils.Assert;
import org.liuyehcf.compile.engine.expression.core.model.OperatorType;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/10/2
 */
public class DelegateOperatorFunction extends OperatorFunction {

    /**
     * uuid
     */
    private final String id;

    /**
     * delegated operator function
     */
    private final OperatorFunction operatorFunction;

    public DelegateOperatorFunction(String id, OperatorFunction operatorFunction) {
        Assert.assertNotNull(id);
        Assert.assertNotNull(operatorFunction);
        this.id = id;
        this.operatorFunction = operatorFunction;
    }

    public final String getId() {
        return id;
    }

    public final OperatorFunction getOperatorFunction() {
        return operatorFunction;
    }

    @Override
    public final OperatorType getType() {
        return operatorFunction.getType();
    }

    @Override
    public final int getOrder() {
        return operatorFunction.getOrder();
    }

    @Override
    public final boolean accept(ExpressionValue arg) {
        return operatorFunction.accept(arg);
    }

    @Override
    public final boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return operatorFunction.accept(arg1, arg2);
    }

    @Override
    public final ExpressionValue call(ExpressionValue arg) {
        return operatorFunction.call(arg);
    }

    @Override
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        return operatorFunction.call(arg1, arg2);
    }
}
