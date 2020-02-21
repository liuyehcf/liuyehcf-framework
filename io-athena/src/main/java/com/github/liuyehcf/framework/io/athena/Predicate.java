package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public interface Predicate {

    /**
     * The predicate to evaluate.
     *
     * @param o an instance that the predicate is evaluated on.
     * @return the result of the predicate
     */
    boolean match(Object o);
}
