package com.github.liuyehcf.framework.rule.engine.model;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public interface Element extends Serializable {

    /**
     * Unique id within the scope of the rule
     *
     * @return unique id
     */
    String getId();

    /**
     * element type
     *
     * @return type
     */
    ElementType getType();

    /**
     * bind rule
     *
     * @param rule rule
     */
    void bindRule(Rule rule);

    /**
     * unbind rule
     */
    void unbindRule();

    /**
     * get bound rule
     *
     * @return bound rule
     */
    Rule getRule();
}
