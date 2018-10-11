package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class StringSplitFunction extends Function {
    @Override
    public String getName() {
        return "string.split";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object sequence = arg1.getValue();
        Object regex = arg2.getValue();

        if (sequence == null || regex == null) {
            return ExpressionValue.valueOf(new String[0]);
        }

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (!(regex instanceof String)) {
            throw createTypeIllegalException(2, regex);
        }

        String[] segments = ((String) sequence).split((String) regex);

        return ExpressionValue.valueOf(segments);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        String sequence = arg1.getValue();
        String regex = arg2.getValue();
        Long limit = arg3.getValue();

        if (sequence == null) {
            throw new ExpressionException(getName() + "'s first arg is null");
        }

        if (regex == null) {
            throw new ExpressionException(getName() + "'s second arg is null");
        }

        if (limit == null) {
            throw new ExpressionException(getName() + "'s third arg is null");
        }

        String[] segments = sequence.split(regex, limit.intValue());

        return ExpressionValue.valueOf(segments);
    }
}
