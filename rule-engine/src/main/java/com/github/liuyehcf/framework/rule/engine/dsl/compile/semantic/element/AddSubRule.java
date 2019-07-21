package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.rule.engine.model.Rule;

/**
 * @author hechenfeng
 * @date 2019/7/3
 */
public class AddSubRule extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int BLOCK_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        Rule rule = context.addSubRule();

        context.setAttr(BLOCK_STACK_OFFSET, AttrName.NODE, rule);
    }
}
