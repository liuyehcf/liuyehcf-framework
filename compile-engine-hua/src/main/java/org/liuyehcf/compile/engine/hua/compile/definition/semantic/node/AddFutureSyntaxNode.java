package org.liuyehcf.compile.engine.hua.compile.definition.semantic.node;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 创建一个Future语法树节点，仅用于标记非终结符
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class AddFutureSyntaxNode extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    public AddFutureSyntaxNode(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        context.getStack().addFutureSyntaxNode(stackOffset);
    }
}
