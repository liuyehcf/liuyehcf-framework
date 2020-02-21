package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public interface Apply<I> {

    /**
     * The application to perform.
     *
     * @param i an instance that the application is performed on
     */
    void apply(I i) throws Exception;
}
