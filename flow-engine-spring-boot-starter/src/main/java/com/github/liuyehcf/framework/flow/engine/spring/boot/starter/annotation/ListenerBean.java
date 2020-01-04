package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ListenerBean {

    /**
     * bunch of action names
     */
    String[] names();

    /**
     * regex of matched engine instance name
     * matches all engine instances by default
     */
    String engineNameRegex() default ".*";
}
