package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.rpc.ares.constant.SchemaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresConsumer {

    /**
     * http schema
     */
    SchemaType schema() default SchemaType.http;

    /**
     * http host
     */
    String host();

    /**
     * http port
     */
    String port();
}
