package com.github.liuyehcf.framework.flow.engine.util;

import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinMode;

/**
 * @author hechenfeng
 * @date 2019/5/15
 */
public abstract class ElementUtils {

    public static boolean isOrJoinMode(JoinGateway joinGateway) {
        return JoinMode.or.equals(joinGateway.getJoinMode());
    }
}
