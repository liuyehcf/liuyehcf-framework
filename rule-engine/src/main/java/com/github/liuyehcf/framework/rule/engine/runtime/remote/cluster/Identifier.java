package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public interface Identifier {

    static boolean equals(Identifier identifier1, Identifier identifier2) {
        return Objects.equals(identifier1.getIdentifier(), identifier2.getIdentifier());
    }

    /**
     * get identifier
     */
    String getIdentifier();
}
