package com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class MemberConfig implements MemberIdentifier {

    private String host;
    private Integer port;

    public MemberConfig() {
    }

    public MemberConfig(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public final String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public final Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("MemberConfig(host='%s', port='%d')", host, port);
    }
}
