package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.join;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinMode;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class PopJoinScope extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int JOIN_MODE_STACK_OFFSET = -2;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int GATEWAY_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        JoinMode joinMode = context.getAttr(JOIN_MODE_STACK_OFFSET, AttrName.JOIN_MODE);

        JoinGateway joinGateway = context.addJoinGateway(joinMode);

        context.setAttr(GATEWAY_STACK_OFFSET, AttrName.NODE, joinGateway);
    }
}
