package com.github.liuyehcf.framework.rule.engine.model.activity;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.AbstractNode;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public abstract class AbstractActivity extends AbstractNode implements Activity {

    private static final long serialVersionUID = -6109669779465660594L;

    private final String name;
    private final String[] argumentNames;
    private final Object[] argumentValues;

    AbstractActivity(String id, LinkType linkType, String name, String[] argumentNames, Object[] argumentValues) {
        super(id, linkType);
        Assert.assertNotNull(name, "name");
        Assert.assertNotNull(argumentNames, "argumentNames");
        Assert.assertNotNull(argumentValues, "argumentValues");
        this.name = name;
        this.argumentNames = argumentNames;
        this.argumentValues = argumentValues;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String[] getArgumentNames() {
        return argumentNames;
    }

    @Override
    public final Object[] getArgumentValues() {
        return argumentValues;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getType().getType(), getId(), getName());
    }
}
