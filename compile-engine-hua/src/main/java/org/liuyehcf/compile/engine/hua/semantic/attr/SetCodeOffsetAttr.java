package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 设置代码偏移量
 *
 * @author chenlu
 * @date 2018/6/19
 */
public class SetCodeOffsetAttr extends AbstractSemanticAction {

    /**
     * 属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    public SetCodeOffsetAttr(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        context.getStack().get(stackOffset).put(AttrName.CODE_OFFSET.name(),
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
    }
}
