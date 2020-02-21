package com.github.liuyehcf.framework.io.athena.event;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class LeaderStatusQueryResponse implements SystemEvent {

    private final long id;
    private final long version;
    private final boolean isHealthy;

    public LeaderStatusQueryResponse(long id, long version, boolean isHealthy) {
        this.id = id;
        this.version = version;
        this.isHealthy = isHealthy;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public boolean isHealthy() {
        return isHealthy;
    }
}
