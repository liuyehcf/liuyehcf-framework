package com.github.liuyehcf.framework.flow.engine.runtime.constant;

/**
 * @author hechenfeng
 * @date 2019/10/17
 */
public enum EnvCloneType {

    /**
     * shallow copy which only copy first level of env, the origin and clone one share values
     * speed: fast
     * isolation: bad
     */
    shallow,

    /**
     * bean copy which only copy properties with set/get methods and map and collection
     * speed: fast
     * isolation: normal
     */
    bean,

    /**
     * kryo serialization which only copy fields
     * speed: middle
     * isolation: normal
     */
    kryo,

    /**
     * hessian serialization which will clone all object network
     * speed: slow
     * isolation: good
     */
    hessian,

    /**
     * java default built-in serialization which will clone all object network
     * speed: slow
     * isolation: good
     */
    java,
}
