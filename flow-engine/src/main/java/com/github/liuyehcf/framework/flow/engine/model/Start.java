package com.github.liuyehcf.framework.flow.engine.model;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public class Start extends AbstractNode {

    private static final long serialVersionUID = -5352822240544957283L;

    public Start(String id) {
        super(id, LinkType.NORMAL);
    }

    @Override
    public final ElementType getType() {
        return ElementType.START;
    }
}
