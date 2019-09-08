package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class ClusterNodeConfig implements ClusterNodeIdentifier {

    private String host;
    private Integer port;

    public ClusterNodeConfig() {
    }

    public ClusterNodeConfig(String host, Integer port) {
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
}
