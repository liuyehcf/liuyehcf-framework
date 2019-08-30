package com.github.liuyehcf.framework.rule.engine.model.listener;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public enum ListenerScope {

    /**
     * node scope, including
     *
     * @see com.github.liuyehcf.framework.rule.engine.model.activity.Action
     * @see com.github.liuyehcf.framework.rule.engine.model.activity.Condition
     * @see com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway
     * @see com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway
     * @see com.github.liuyehcf.framework.rule.engine.model.Rule
     */
    node,

    /**
     * global scope
     */
    global
}
