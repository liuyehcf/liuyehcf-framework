package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

/**
 * @author hechenfeng
 * @date 2019/5/12
 */
public interface Attribute {

    /**
     * get name of attribute
     */
    String getName();

    /**
     * get value of attribute
     */
    <T> T getValue();
}
