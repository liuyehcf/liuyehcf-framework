package com.github.liuyehcf.framework.rpc.maple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenlu
 * @date 2019/3/6
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MapleConsumer {

    /**
     * Maple service group
     */
    String serviceGroup() default MapleConst.DEFAULT_GROUP;

    /**
     * Maple service version.
     */
    String serviceVersion() default MapleConst.DEFAULT_VERSION;

    /**
     * Maple client timeout
     */
    long clientTimeout() default MapleConst.DEFAULT_CLIENT_TIMEOUT;
}
