package com.github.liuyehcf.framework.io.athena.processor;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public interface Processor<I> {

    /**
     * determine whether the event is match this processor
     *
     * @param event event
     * @return whether matches
     */
    boolean match(Object event);

    /**
     * process received event
     *
     * @param context event context
     */
    void process(EventContext<I> context) throws Exception;
}
