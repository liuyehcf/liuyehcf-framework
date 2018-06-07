package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 创建一个Future语法树节点，仅用于标记非终结符
 *
 * @author chenlu
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

    public int getStackOffset() {
        return stackOffset;
    }
}