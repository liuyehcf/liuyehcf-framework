package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.rpc.ares.constant.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresRequestBody {

    /**
     * content type
     *
     * @see org.apache.http.entity.ContentType
     */
    String contentType() default "application/json; charset=UTF-8";

    /**
     * serialize type
     */
    SerializeType serializeType() default SerializeType.fastjson;
}