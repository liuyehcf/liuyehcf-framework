package com.github.liuyehcf.framework.flow.engine.model.activity;

import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DefaultAction extends AbstractActivity implements Action {

    private static final long serialVersionUID = 773619763069359981L;

    public DefaultAction(String id, LinkType linkType, String name, String[] argumentNames, Object[] argumentValues) {
        super(id, linkType, name, argumentNames, argumentValues);
    }

    @Override
    public final ElementType getType() {
        return ElementType.ACTION;
    }
}
