package com.github.liuyehcf.framework.io.athena.event;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class LeaderKeepAlive implements SystemEvent {

    private final String clusterAbstractInfo;
    private final long version;

    public LeaderKeepAlive(String clusterAbstractInfo, long version) {
        this.clusterAbstractInfo = clusterAbstractInfo;
        this.version = version;
    }

    public String getClusterAbstractInfo() {
        return clusterAbstractInfo;
    }

    public long getVersion() {
        return version;
    }
}
