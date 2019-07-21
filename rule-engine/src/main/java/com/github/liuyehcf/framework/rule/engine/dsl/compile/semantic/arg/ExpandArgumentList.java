package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.arg;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class ExpandArgumentList extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int ARGUMENT_STACK_OFFSET = 0;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int ARGUMENT_LIST_STACK_OFFSET = -2;

    @Override
    public void onAction(CompilerContext context) {
        List<String> argumentNameList = context.getAttr(ARGUMENT_LIST_STACK_OFFSET, AttrName.ARGUMENT_NAME_LIST);
        String argumentName = context.getAttr(ARGUMENT_STACK_OFFSET, AttrName.ARGUMENT_NAME);
        argumentNameList.add(argumentName);

        List<Object> argumentValueList = context.getAttr(ARGUMENT_LIST_STACK_OFFSET, AttrName.ARGUMENT_VALUE_LIST);
        Object argumentValue = context.getAttr(ARGUMENT_STACK_OFFSET, AttrName.ARGUMENT_VALUE);
        argumentValueList.add(argumentValue);

        context.setAttrToLeftNode(AttrName.ARGUMENT_NAME_LIST, argumentNameList);
        context.setAttrToLeftNode(AttrName.ARGUMENT_VALUE_LIST, argumentValueList);
    }
}
