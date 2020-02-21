package com.github.liuyehcf.framework.io.athena.event;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class LeaderQuery implements SystemEvent {

    private final long id;

    public LeaderQuery(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
