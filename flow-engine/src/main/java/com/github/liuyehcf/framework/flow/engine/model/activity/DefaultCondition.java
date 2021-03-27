package com.github.liuyehcf.framework.flow.engine.model.activity;

import com.github.liuyehcf.framework.flow.engine.model.ElementType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DefaultCondition extends AbstractActivity implements Condition {

    private static final long serialVersionUID = -7902093925729684575L;

    public DefaultCondition(String id, String name, String[] argumentNames, Object[] argumentValues) {
        super(id, name, argumentNames, argumentValues);
    }

    @Override
    public final ElementType getType() {
        return ElementType.CONDITION;
    }
}

