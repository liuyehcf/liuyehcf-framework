package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class DefaultClusterNode implements ClusterNode {

    private final String host;
    private final int port;
    private final boolean isSeed;
    private final ClusterNodeStatus status;
    private final long version;

    public DefaultClusterNode(String host, int port, boolean isSeed, ClusterNodeStatus status) {
        this(host, port, isSeed, status, System.nanoTime());
    }

    private DefaultClusterNode(String host, int port, boolean isSeed, ClusterNodeStatus status, long version) {
        this.host = host;
        this.port = port;
        this.isSeed = isSeed;
        this.status = status;
        this.version = version;
    }

    @Override
    public final String getHost() {
        return host;
    }

    @Override
    public final Integer getPort() {
        return port;
    }

    @Override
    public final boolean isSeed() {
        return isSeed;
    }

    @Override
    public final ClusterNodeStatus getStatus() {
        return status;
    }

    @Override
    public final long getVersion() {
        return version;
    }

    @Override
    public final ClusterNode clone(ClusterNodeStatus status) {
        return new DefaultClusterNode(host, port, isSeed, status == null ? this.status : status);
    }

    @Override
    public String toString() {
        return String.format("Node(address = '%s', status = '%s')", getIdentifier(), getStatus());
    }
}
