package com.github.liuyehcf.framework.expression.engine.core.bytecode.cp;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.core.function.OperatorFunction;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;
import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

import java.util.List;

/**
 * 运算指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public abstract class Compute extends ByteCode {

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    @Override
    @JSONField(serialize = false)
    public final Object[] getOperators() {
        return OPERATORS;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }

    public abstract int getStackOperandNum();

    final void operateForTwoOperand(RuntimeContext context, OperatorType type) {
        List<OperatorFunction> operatorFunctions = ExpressionEngine.getOperatorFunctions(type);

        ExpressionValue value2 = context.pop();
        ExpressionValue value1 = context.pop();

        for (OperatorFunction operatorFunction : operatorFunctions) {
            if (operatorFunction.accept(value1, value2)) {
                ExpressionValue result = operatorFunction.call(value1, value2);

                /*
                 * 特殊处理一下Cmp，将operator function的返回值改造成 ComparableValue
                 */
                if (this instanceof _cmp) {
                    Object resultValue = result.getValue();

                    if (!(resultValue instanceof Long)) {
                        throw new ExpressionException("CmpOperatorFunction's return type must be 'java.lang.Long' (or compatible type 'java.lang.Byte', 'java.lang.Short', 'java.lang.Integer')");
                    }

                    context.push(ExpressionValue.valueOf(ComparableValue.valueOf((long) resultValue)));
                } else {
                    context.push(result);
                }

                context.increaseCodeOffset();
                return;
            }
        }

        throw createExpressionException(type, value1, value2);
    }

    final void operateForOneOperand(RuntimeContext context, OperatorType type) {
        List<OperatorFunction> operatorFunctions = ExpressionEngine.getOperatorFunctions(type);

        ExpressionValue value = context.pop();

        for (OperatorFunction operatorFunction : operatorFunctions) {
            if (operatorFunction.accept(value)) {
                ExpressionValue result = operatorFunction.call(value);
                context.push(result);
                context.increaseCodeOffset();
                return;
            }
        }

        throw createExpressionException(type, value);
    }

    private ExpressionException createExpressionException(OperatorType type, ExpressionValue... values) {
        if (values.length == 1) {
            return new ExpressionException("could not find compatible operator(" + type.getSymbol() + ") function. type='"
                    + (values[0].getValue() == null ? null : values[0].getValue().getClass().getName()) + "'");
        } else {
            return new ExpressionException("could not find compatible operator(" + type.getSymbol() + ") function. type1='"
                    + (values[0].getValue() == null ? null : values[0].getValue().getClass().getName()) + "', type2='"
                    + (values[1].getValue() == null ? null : values[1].getValue().getClass().getName()) + "'");
        }
    }
}
