package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class PopIteratorLinkType extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.popLinkType();
    }
}
