package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class StringSubStringFunction extends Function {

    @Override
    public String getName() {
        return "string.substring";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object sequence = arg1.getValue();
        Object beginIndex = arg2.getValue();

        if (sequence == null) {
            return ExpressionValue.valueOf(null);
        }

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (beginIndex == null) {
            return ExpressionValue.valueOf(sequence);
        }

        if (!(beginIndex instanceof Long)) {
            throw createTypeIllegalException(2, beginIndex);
        }

        return ExpressionValue.valueOf(((String) sequence).substring(((Long) beginIndex).intValue()));
    }

    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        Object sequence = arg1.getValue();
        Object beginIndex = arg2.getValue();
        Object endIndex = arg3.getValue();

        if (sequence == null) {
            return ExpressionValue.valueOf(null);
        }

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (beginIndex == null) {
            return ExpressionValue.valueOf(sequence);
        }
        if (!(beginIndex instanceof Long)) {
            throw createTypeIllegalException(2, beginIndex);
        }

        if (endIndex == null) {
            return ExpressionValue.valueOf(((String) sequence).substring(((Long) beginIndex).intValue()));
        }
        if (!(endIndex instanceof Long)) {
            throw createTypeIllegalException(3, endIndex);
        }

        return ExpressionValue.valueOf(((String) sequence).substring(((Long) beginIndex).intValue(), ((Long) endIndex).intValue()));
    }
}
