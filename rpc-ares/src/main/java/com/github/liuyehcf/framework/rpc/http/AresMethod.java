package com.github.liuyehcf.framework.rpc.http;

import com.github.liuyehcf.framework.rpc.http.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.http.constant.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenfeng.hcf
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
     * deserialize type of response body
     */
    SerializeType responseDeserializeType() default SerializeType.string;
}
