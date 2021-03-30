package com.github.liuyehcf.framework.flow.engine.model;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.event.Event;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public abstract class AbstractElement implements Element {

    private static final long serialVersionUID = -8417584529613001900L;

    private final String id;
    private Flow flow;

    AbstractElement(String id) {
        Assert.assertNotNull(id, "id");
        this.id = id;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final void bindFlow(Flow flow) {
        if (this.flow != null) {
            unbindFlow();
        }
        this.flow = flow;
        this.flow.addElement(this);

        if (this instanceof Listener) {
            this.flow.addListener((Listener) this);
        } else if (this instanceof Event) {
            this.flow.addEvent((Event) this);
        }
    }

    @Override
    public final void unbindFlow() {
        Flow removedFlow = this.flow;
        this.flow = null;
        if (removedFlow != null) {
            removedFlow.removeElement(this);

            if (this instanceof Listener) {
                removedFlow.removeListener((Listener) this);
            } else if (this instanceof Event) {
                removedFlow.removeEvent((Event) this);
            }
        }
    }

    @Override
    public final Flow getFlow() {
        return flow;
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
