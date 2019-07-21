package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

/**
 * @author hechenfeng
 * @date 2019/5/12
 */
public interface Argument {

    /**
     * get name of argument
     */
    String getName();

    /**
     * get value of argument
     */
    Object getValue();
}
