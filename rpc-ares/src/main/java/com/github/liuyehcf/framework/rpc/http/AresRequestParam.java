package com.github.liuyehcf.framework.rpc.http;

import com.github.liuyehcf.framework.rpc.http.constant.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresRequestParam {

    /**
     * request param name
     */
    String name();

    /**
     * serialize type
     */
    SerializeType serializeType() default SerializeType.json;
}
