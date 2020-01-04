package com.github.liuyehcf.framework.flow.engine.model;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public abstract class AbstractAttachable extends AbstractElement implements Attachable {

    private static final long serialVersionUID = -5014960792509003175L;

    private final String attachedId;

    protected AbstractAttachable(String id, String attachedId) {
        super(id);
        this.attachedId = attachedId;
    }

    @Override
    public final String getAttachedId() {
        return attachedId;
    }
}
