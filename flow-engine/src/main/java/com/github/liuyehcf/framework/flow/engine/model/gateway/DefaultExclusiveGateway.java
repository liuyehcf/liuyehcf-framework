package com.github.liuyehcf.framework.flow.engine.model.gateway;

import com.github.liuyehcf.framework.flow.engine.model.ElementType;

/**
 * @author hechenfeng
 * @date 2019/5/9
 */
public class DefaultExclusiveGateway extends AbstractGateway implements ExclusiveGateway {

    private static final long serialVersionUID = -1915224219424142701L;

    public DefaultExclusiveGateway(String id) {
        super(id);
    }

    @Override
    public final ElementType getType() {
        return ElementType.EXCLUSIVE_GATEWAY;
    }
}
