package com.github.liuyehcf.framework.flow.engine.model.gateway;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
public enum JoinMode {

    /**
     * All predecessors of joinGateway must arrive
     * and if some links are unreachable, an exception will be thrown.
     */
    hard_and,

    /**
     * All reachable nodes of joinGateway must arrive(Allow unreachable links)
     * an exception will be throw if all the predecessors are unreachable
     */
    soft_and,

    /**
     * any predecessors of joinGateway arrive
     */
    or,
}
