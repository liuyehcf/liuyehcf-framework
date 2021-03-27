package com.github.liuyehcf.framework.flow.engine.model.gateway;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class DefaultJoinGateway extends AbstractGateway implements JoinGateway {

    private static final long serialVersionUID = -564622674949951694L;

    private final JoinMode joinMode;

    public DefaultJoinGateway(String id, JoinMode joinMode) {
        super(id);
        Assert.assertNotNull(joinMode, "joinMode");
        this.joinMode = joinMode;
    }

    @Override
    public final ElementType getType() {
        return ElementType.JOIN_GATEWAY;
    }

    @Override
    public final JoinMode getJoinMode() {
        return joinMode;
    }
}
