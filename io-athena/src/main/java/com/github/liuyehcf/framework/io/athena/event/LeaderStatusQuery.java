package com.github.liuyehcf.framework.io.athena.event;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class LeaderStatusQuery implements SystemEvent {

    private final long id;
    private final long version;

    public LeaderStatusQuery(long id, long version) {
        this.id = id;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}
