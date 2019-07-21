package com.github.liuyehcf.framework.rpc.maple;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/3/6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MapleProvider {

    /**
     * Maple service interface
     */
    Class<?> serviceInterface();

    /**
     * Maple service group
     */
    String serviceGroup() default MapleConst.DEFAULT_GROUP;

    /**
     * Maple service version
     */
    String serviceVersion() default MapleConst.DEFAULT_VERSION;

    /**
     * Maple serialize type
     *
     * @see SerializeType
     */
    String serializeType() default MapleConst.DEFAULT_SERIALIZE_TYPE;
}
