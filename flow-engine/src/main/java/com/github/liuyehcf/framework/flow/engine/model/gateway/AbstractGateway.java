package com.github.liuyehcf.framework.flow.engine.model.gateway;

import com.github.liuyehcf.framework.flow.engine.model.AbstractNode;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/5/15
 */
public abstract class AbstractGateway extends AbstractNode implements Gateway {

    private static final long serialVersionUID = -1198637204382902085L;

    AbstractGateway(String id, LinkType linkType) {
        super(id, linkType);
    }
}
