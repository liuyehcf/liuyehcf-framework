package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.io.athena.Identifier;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class LeaderQueryResponse implements SystemEvent {

    private final long id;
    private final Identifier identifier;

    public LeaderQueryResponse(long id, Identifier identifier) {
        this.id = id;
        if (identifier != null) {
            this.identifier = identifier.copy();
        } else {
            this.identifier = null;
        }
    }

    public long getId() {
        return id;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
