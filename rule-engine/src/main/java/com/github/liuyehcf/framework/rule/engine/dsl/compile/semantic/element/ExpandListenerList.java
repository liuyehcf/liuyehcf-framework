package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class ExpandListenerList extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int listenerStackOffset;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int listenerListStackOffset;

    public ExpandListenerList(int listenerStackOffset, int listenerListStackOffset) {
        this.listenerStackOffset = listenerStackOffset;
        this.listenerListStackOffset = listenerListStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Listener listener = context.getAttr(listenerStackOffset, AttrName.LISTENER);
        List<Listener> listenerList = context.getAttr(listenerListStackOffset, AttrName.LISTENER_LIST);

        listenerList.add(listener);
    }
}
