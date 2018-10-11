package org.liuyehcf.compile.engine.expression.compile.optimize;

import org.liuyehcf.compile.engine.expression.core.ExpressionCode;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public interface Optimizer extends Serializable {
    void optimize(final ExpressionCode expressionCode);
}
