package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public interface ClusterNodeIdentifier extends Identifier {

    /**
     * host of cluster node
     */
    String getHost();

    /**
     * port of cluster node
     */
    Integer getPort();

    /**
     * unique identifier of cluster node
     */
    default String getIdentifier() {
        return String.format("%s:%d", getHost(), getPort());
    }
}
