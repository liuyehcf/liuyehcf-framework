package com.github.liuyehcf.framework.rule.engine.model.activity;

import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DefaultCondition extends AbstractActivity implements Condition {

    private static final long serialVersionUID = -7902093925729684575L;

    public DefaultCondition(String id, LinkType linkType, String name, String[] argumentNames, Object[] argumentValues) {
        super(id, linkType, name, argumentNames, argumentValues);
    }

    @Override
    public final ElementType getType() {
        return ElementType.CONDITION;
    }
}

