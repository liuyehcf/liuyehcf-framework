package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.join;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class AddJoinNode extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int SUB_FLOW_JOIN_MARK_TACK_OFFSET = 0;


    private final LinkType linkType;

    public AddJoinNode(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public void onAction(CompilerContext context) {
        if (linkType != null) {
            context.addJoinNode(context.peekNode(), linkType);
        } else {
            LinkType subFlowJoinMarkLinkType = context.getAttr(SUB_FLOW_JOIN_MARK_TACK_OFFSET, AttrName.SUB_FLOW_JOIN_MARK);
            if (subFlowJoinMarkLinkType != null) {
                context.addJoinNode(context.peekNode(), subFlowJoinMarkLinkType);
            }
        }
    }
}
