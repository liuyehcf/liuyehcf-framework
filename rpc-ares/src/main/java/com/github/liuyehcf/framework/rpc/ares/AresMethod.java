package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.ares.constant.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresMethod {

    /**
     * path of http request
     */
    String path();

    /**
     * optional value is GET/POST
     *
     * @see HttpMethod
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * content type
     *
     * @see org.apache.http.entity.ContentType
     */
    String contentType() default "application/json; charset=UTF-8";

    /**
     * deserialize type of response body
     */
    SerializeType responseDeserializeType() default SerializeType.fastjson;
}
