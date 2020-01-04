package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.google.common.collect.Lists;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class InitListenerList extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.setAttrToLeftNode(AttrName.LISTENER_LIST, Lists.newArrayList());
    }
}
