package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class Identifier extends Address {

    /**
     * redundant information just for log
     */
    private final String name;

    public Identifier(Identifier identifier) {
        this(identifier.name, identifier.getHost(), identifier.getPort());
    }

    public Identifier(String name, String host, int port) {
        super(host, port);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public final String toString() {
        return String.format("%s@%s:%d", name, getHost(), getPort());
    }

    public Identifier copy() {
        return new Identifier(this);
    }
}
