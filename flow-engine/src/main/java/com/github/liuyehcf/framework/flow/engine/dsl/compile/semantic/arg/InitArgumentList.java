package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.arg;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.google.common.collect.Lists;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class InitArgumentList extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int ARGUMENT_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        String argumentName = context.getAttr(ARGUMENT_STACK_OFFSET, AttrName.ARGUMENT_NAME);
        Object argumentValue = context.getAttr(ARGUMENT_STACK_OFFSET, AttrName.ARGUMENT_VALUE);

        context.setAttrToLeftNode(AttrName.ARGUMENT_NAME_LIST, Lists.newArrayList(argumentName));
        context.setAttrToLeftNode(AttrName.ARGUMENT_VALUE_LIST, Lists.newArrayList(argumentValue));
    }
}
