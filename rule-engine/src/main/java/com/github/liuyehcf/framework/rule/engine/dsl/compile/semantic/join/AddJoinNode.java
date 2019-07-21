package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.join;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.rule.engine.model.Node;

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
    private final int nodeStackOffset;

    public AddJoinNode(int nodeStackOffset) {
        this.nodeStackOffset = nodeStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Node node = context.getAttr(nodeStackOffset, AttrName.NODE);

        context.addJoinNode(node);
    }
}
