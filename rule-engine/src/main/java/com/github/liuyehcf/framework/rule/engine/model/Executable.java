package com.github.liuyehcf.framework.rule.engine.model;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public interface Executable {

    /**
     * executable name
     *
     * @return activity name
     */
    String getName();

    /**
     * array of argument name
     *
     * @return argument names
     */
    String[] getArgumentNames();

    /**
     * array of argument value
     *
     * @return argument values
     */
    Object[] getArgumentValues();
}
