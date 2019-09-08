package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public interface ClusterNode extends ClusterNodeIdentifier {

    /**
     * whether seed
     */
    boolean isSeed();

    /**
     * status of cluster node
     */
    ClusterNodeStatus getStatus();

    /**
     * version of this node
     */
    long getVersion();

    /**
     * clone all fields except status
     *
     * @param status origin status will be used if status is null
     */
    ClusterNode clone(ClusterNodeStatus status);
}
