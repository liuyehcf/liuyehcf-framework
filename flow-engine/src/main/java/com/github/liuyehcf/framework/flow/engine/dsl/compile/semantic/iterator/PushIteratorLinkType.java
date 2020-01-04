package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class PushIteratorLinkType extends AbstractSemanticAction {

    private final LinkType linkType;

    public PushIteratorLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public void onAction(CompilerContext context) {
        context.pushLinkType(linkType);
    }
}
