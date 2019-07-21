package com.github.liuyehcf.framework.rule.engine.model.gateway;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public interface JoinGateway extends Gateway {

    /**
     * join type of this join gateway
     *
     * @see JoinMode
     */
    JoinMode getJoinMode();
}
