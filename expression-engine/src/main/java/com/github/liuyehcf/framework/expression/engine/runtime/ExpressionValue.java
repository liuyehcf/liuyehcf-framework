package com.github.liuyehcf.framework.expression.engine.runtime;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public interface ExpressionValue {

    static ExpressionValue valueOf(Object object) {
        return ExpressionValueImpl.valueOf(object);
    }

    /**
     * 返回value，类型相关
     */
    <T> T getValue();
}
