package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public interface MemberIdentifier extends Identifier {

    /**
     * host of cluster member
     */
    String getHost();

    /**
     * port of cluster member
     */
    Integer getPort();

    /**
     * unique identifier of cluster member
     */
    default String getIdentifier() {
        return String.format("%s:%d", getHost(), getPort());
    }
}
