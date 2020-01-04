package com.github.liuyehcf.framework.flow.engine.model;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public interface Element extends Serializable {

    /**
     * Unique id within the scope of the flow
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
     * bind flow
     *
     * @param flow flow
     */
    void bindFlow(Flow flow);

    /**
     * unbind flow
     */
    void unbindFlow();

    /**
     * get bound flow
     *
     * @return bound flow
     */
    Flow getFlow();
}
