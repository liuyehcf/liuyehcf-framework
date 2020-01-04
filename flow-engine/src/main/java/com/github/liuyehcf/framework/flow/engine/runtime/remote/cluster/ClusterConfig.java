package com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class ClusterConfig {

    private List<MemberConfig> seeds;
    private MemberConfig self;
    private Integer idleTime;
    private Integer heartbeatInterval;
    private Integer heartbeatRetryInterval;
    private Integer topologyProbeInterval;
    private String serializeType;

    public List<MemberConfig> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<MemberConfig> seeds) {
        this.seeds = seeds;
    }

    public MemberConfig getSelf() {
        return self;
    }

    public void setSelf(MemberConfig self) {
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

    public Integer getTopologyProbeInterval() {
        return topologyProbeInterval;
    }

    public void setTopologyProbeInterval(Integer topologyProbeInterval) {
        this.topologyProbeInterval = topologyProbeInterval;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }
}
