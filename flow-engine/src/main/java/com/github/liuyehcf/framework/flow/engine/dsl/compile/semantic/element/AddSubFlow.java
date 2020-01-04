package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.Flow;

/**
 * @author hechenfeng
 * @date 2019/7/3
 */
public class AddSubFlow extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int BLOCK_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        Flow flow = context.addSubFlow();

        context.setAttr(BLOCK_STACK_OFFSET, AttrName.NODE, flow);
    }
}
