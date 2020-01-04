package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Identifier;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public class Greet implements Message, Identifier {

    private final String host;
    private final int port;

    public Greet(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public final MessageType getType() {
        return MessageType.GREET;
    }

    @Override
    public final String getIdentifier() {
        return String.format("%s:%d", host, port);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
