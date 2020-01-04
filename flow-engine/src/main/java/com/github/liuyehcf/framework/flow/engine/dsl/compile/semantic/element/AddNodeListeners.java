package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class AddNodeListeners extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int listenerListStackOffset;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int nodeStackOffset;

    public AddNodeListeners(int listenerListStackOffset, int nodeStackOffset) {
        this.listenerListStackOffset = listenerListStackOffset;
        this.nodeStackOffset = nodeStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        List<Listener> listeners = context.getAttr(listenerListStackOffset, AttrName.LISTENER_LIST);
        Node node = context.getAttr(nodeStackOffset, AttrName.NODE);

        listeners.forEach(node::addListener);
    }
}
