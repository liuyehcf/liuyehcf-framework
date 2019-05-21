package com.github.liuyehcf.framework.expression.engine.core.function.date;


import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

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
