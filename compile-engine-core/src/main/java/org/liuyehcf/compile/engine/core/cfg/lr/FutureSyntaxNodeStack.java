package org.liuyehcf.compile.engine.core.cfg.lr;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.LinkedList;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNull;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * Future 语法树栈
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class FutureSyntaxNodeStack {
    private int top = -1;
    private LinkedList<SyntaxNode> stack = new LinkedList<>();

    /**
     * 添加正常语法树节点
     */
    void addNormalSyntaxNode(Symbol id, String value) {
        /*
         * 如果之前已经创建过Future语法树节点
         * 由于标记非终结符要为未来的语法树节点设置继承属性，这些语法树节点称为Future语法树节点
         */
        if (size() < stack.size()) {
            top++;
            SyntaxNode node = stack.get(top);

            assertNull(node.getId());
            assertNull(node.getValue());

            node.setId(id);
            node.setValue(value);
        } else if (size() == stack.size()) {
            stack.addLast(new SyntaxNode(id, value));
            top++;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 增加Future语法树节点
     *
     * @param stackOffset 相对于top的偏移量
     */
    public void addFutureSyntaxNode(int stackOffset) {
        assertTrue(stackOffset > 0);
        if (top + stackOffset <= stack.size() - 1) {
            return;
        }
        while (top + stackOffset > stack.size() - 1) {
            stack.addLast(new SyntaxNode());
        }
    }

    /**
     * 压入正常语法树节点
     */
    void pushNormalSyntaxNode(SyntaxNode node) {
        stack.add(++top, node);
    }

    /**
     * 返回当前实际语法树节点数，不包括Future语法树节点
     */
    public int size() {
        return top + 1;
    }

    /**
     * 弹出实际栈顶元素，不包括Future语法树节点
     */
    void pop() {
        if (size() < stack.size()) {
            stack.remove(top--);
        } else {
            --top;
            stack.removeLast();
        }
    }

    /**
     * 返回实际栈顶元素，不包括Future语法树节点
     */
    SyntaxNode peek() {
        if (top > -1) {
            return stack.get(top);
        }
        return null;
    }

    /**
     * 是否为空，不包括Future语法树节点
     */
    public boolean isEmpty() {
        return top == -1;
    }


    /**
     * 依据偏移量从栈中获取语法树节点
     *
     * @param stackOffset 相对于top的偏移量。例如1表示Future语法树节点，-1表示栈次顶元素
     */
    public SyntaxNode get(int stackOffset) {
        return stack.get(top + stackOffset);
    }
}
