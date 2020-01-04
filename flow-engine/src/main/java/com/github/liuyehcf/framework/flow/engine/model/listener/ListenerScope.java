package com.github.liuyehcf.framework.flow.engine.model.listener;

import com.github.liuyehcf.framework.flow.engine.model.Flow;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public enum ListenerScope {

    /**
     * node scope, including
     *
     * @see com.github.liuyehcf.framework.flow.engine.model.activity.Action
     * @see com.github.liuyehcf.framework.flow.engine.model.activity.Condition
     * @see com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway
     * @see com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway
     * @see Flow
     */
    node,

    /**
     * global scope
     */
    global,
    ;

    public boolean isNode() {
        return node.equals(this);
    }

    public boolean isGlobal() {
        return global.equals(this);
    }
}
