package com.github.liuyehcf.framework.expression.engine.compile.optimize;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public interface Optimizer extends Serializable {
    void optimize(final ExpressionCode expressionCode);
}
