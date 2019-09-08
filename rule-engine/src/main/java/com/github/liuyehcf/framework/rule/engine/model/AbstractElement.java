package com.github.liuyehcf.framework.rule.engine.model;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.event.Event;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public abstract class AbstractElement implements Element {

    private static final long serialVersionUID = -8417584529613001900L;

    private final String id;
    private Rule rule;

    AbstractElement(String id) {
        Assert.assertNotNull(id, "id");
        this.id = id;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final void bindRule(Rule rule) {
        if (this.rule != null) {
            unbindRule();
        }
        this.rule = rule;
        this.rule.addElement(this);

        if (this instanceof Listener) {
            this.rule.addListener((Listener) this);
        } else if (this instanceof Event) {
            this.rule.addEvent((Event) this);
        } else if (this instanceof Rule) {
            ((Rule) this).init();
        }
    }

    @Override
    public final void unbindRule() {
        Rule removedRule = this.rule;
        this.rule = null;
        if (removedRule != null) {
            removedRule.removeElement(this);

            if (this instanceof Listener) {
                removedRule.removeListener((Listener) this);
            } else if (this instanceof Event) {
                removedRule.removeEvent((Event) this);
            }
        }
    }

    @Override
    public final Rule getRule() {
        return rule;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractElement that = (AbstractElement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getType().getType(), getId());
    }
}
