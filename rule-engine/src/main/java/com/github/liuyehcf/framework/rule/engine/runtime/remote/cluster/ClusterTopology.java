package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public interface ClusterTopology extends Identifier, Iterable<ClusterNode> {

    /**
     * get node of specified identifier
     *
     * @return null if absent
     */
    ClusterNode getNode(String identifier);

    /**
     * set or replace node with specified identifier
     */
    void putNode(ClusterNode node);

    /**
     * number of nodes with status active
     */
    int activeNum();

    /**
     * number of nodes with status inactive
     */
    int inactiveNum();

    /**
     * identifier of cluster, depending on the number and status of nodes
     * treat cluster's identifier as md5 or checksum
     */
    @Override
    String getIdentifier();
}
