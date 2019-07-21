package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

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
