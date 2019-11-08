package com.github.liuyehcf.framework.rpc.http;

import com.github.liuyehcf.framework.rpc.http.constant.SchemaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresConsumer {

    /**
     * http schema
     */
    SchemaType schema() default SchemaType.HTTP;

    /**
     * http host
     */
    String host();

    /**
     * http port
     */
    String port();
}
