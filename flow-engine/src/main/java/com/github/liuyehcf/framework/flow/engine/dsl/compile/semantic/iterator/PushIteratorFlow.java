package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class PushIteratorFlow extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int FLOW_NAME_TACK_OFFSET = -3;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int FLOW_ID_TACK_OFFSET = -1;

    private final boolean isSubFlow;

    public PushIteratorFlow(boolean isSubFlow) {
        this.isSubFlow = isSubFlow;
    }

    @Override
    public void onAction(CompilerContext context) {
        if (isSubFlow) {
            context.pushFlow(true, null, null);
        } else {
            // contains flow name and flow id
            if (context.getStack().size() > 2) {
                String flowName = context.getAttr(FLOW_NAME_TACK_OFFSET, AttrName.FLOW_NAME);
                String flowId = context.getAttr(FLOW_ID_TACK_OFFSET, AttrName.FLOW_ID);

                context.pushFlow(false, flowName, flowId);
            } else {
                context.pushFlow(false, null, null);
            }
        }
    }
}
