package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public enum ClusterNodeStatus {

    /**
     * before new member of cluster adding to the cluster
     */
    init,

    /**
     * new member of cluster is adding to the cluster
     * during which some pre hooks(checks for example) will be executed
     */
    joining,

    /**
     * member of cluster is active
     */
    active,

    /**
     * member of cluster is removing from the cluster
     * during which some post hooks will be executed
     */
    leaving,

    /**
     * member of cluster detected inactive
     */
    inactive,

    /**
     * invalid state
     */
    invalid,
    ;
}
