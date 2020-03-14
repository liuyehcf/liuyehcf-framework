package com.github.liuyehcf.framework.flow.engine.runtime.delegate.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldCondition {

    /**
     * whether parse placeholder if delegateField's value is String
     */
    boolean parsePlaceHolder() default true;
}
