package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 递增参数数量
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class IncArgSize extends AbstractSemanticAction {

    /**
     * 参数列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argumentListStackOffset;

    public IncArgSize(int argumentListStackOffset) {
        this.argumentListStackOffset = argumentListStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Integer argumentSize = context.getStack().get(argumentListStackOffset).get(AttrName.ARGUMENT_SIZE.name());
        context.getStack().get(argumentListStackOffset).put(AttrName.ARGUMENT_SIZE.name(), argumentSize + 1);
    }
}
