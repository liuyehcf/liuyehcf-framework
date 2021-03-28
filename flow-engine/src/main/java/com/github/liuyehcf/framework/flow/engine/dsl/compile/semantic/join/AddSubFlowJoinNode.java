package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.join;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2021/3/28
 */
public class AddSubFlowJoinNode extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int SUB_FLOW_JOIN_MARK_TACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        LinkType linkType = context.getAttr(SUB_FLOW_JOIN_MARK_TACK_OFFSET, AttrName.SUB_FLOW_JOIN_MARK);
        if (linkType != null) {
            context.addJoinNode(context.peekNode(), linkType);
        }
    }
}
