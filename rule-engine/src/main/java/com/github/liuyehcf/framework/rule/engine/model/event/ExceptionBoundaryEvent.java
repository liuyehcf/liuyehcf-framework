package com.github.liuyehcf.framework.rule.engine.model.event;

import com.github.liuyehcf.framework.rule.engine.model.AbstractAttachable;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class ExceptionBoundaryEvent extends AbstractAttachable implements Event {

    private static final long serialVersionUID = -4833983775871050108L;

    public ExceptionBoundaryEvent(String id, String attachedId) {
        super(id, attachedId);
    }

    @Override
    public ElementType getType() {
        return ElementType.EVENT;
    }
}
