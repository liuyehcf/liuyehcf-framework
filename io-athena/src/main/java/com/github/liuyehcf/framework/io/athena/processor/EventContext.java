package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Identifier;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class EventContext<T> {

    private final Identifier peer;
    private final T event;

    public EventContext(Identifier peer, T event) {
        Assert.assertNotNull(peer, "peer");
        this.peer = peer;
        this.event = event;
    }

    public Identifier getPeer() {
        return peer;
    }

    public T getEvent() {
        return event;
    }
}
