package org.liuyehcf.compile.engine.expression.core.function.date;


import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class DateTimestampFunction extends Function {
    @Override
    public String getName() {
        return "date.timestamp";
    }

    @Override
    public ExpressionValue call() {
        return ExpressionValue.valueOf(System.currentTimeMillis());
    }
}
