package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class ClusterConfig {

    private List<ClusterNodeConfig> seeds;
    private ClusterNodeConfig self;
    private Integer idleTime;
    private Integer heartbeatInterval;
    private Integer heartbeatRetryInterval;
    private Integer topologyKeepAliveInterval;
    private String serializeType;

    public List<ClusterNodeConfig> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<ClusterNodeConfig> seeds) {
        this.seeds = seeds;
    }

    public ClusterNodeConfig getSelf() {
        return self;
    }

    public void setSelf(ClusterNodeConfig self) {
        this.self = self;
    }

    public Integer getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Integer getHeartbeatRetryInterval() {
        return heartbeatRetryInterval;
    }

    public void setHeartbeatRetryInterval(Integer heartbeatRetryInterval) {
        this.heartbeatRetryInterval = heartbeatRetryInterval;
    }

    public Integer getTopologyKeepAliveInterval() {
        return topologyKeepAliveInterval;
    }

    public void setTopologyKeepAliveInterval(Integer topologyKeepAliveInterval) {
        this.topologyKeepAliveInterval = topologyKeepAliveInterval;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }
}
