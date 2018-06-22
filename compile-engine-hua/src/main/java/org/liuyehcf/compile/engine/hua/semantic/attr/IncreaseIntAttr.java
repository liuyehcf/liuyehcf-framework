package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 递增整数属性
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class IncreaseIntAttr extends AbstractSemanticAction {

    /**
     * 空维度-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    /**
     * 属性-名称
     */
    private final AttrName attrName;

    public IncreaseIntAttr(int stackOffset, AttrName attrName) {
        this.stackOffset = stackOffset;
        this.attrName = attrName;
    }

    @Override
    public void onAction(HuaContext context) {
        int cnt = context.getStack().get(stackOffset).get(attrName.name());

        context.getStack().get(stackOffset).put(attrName.name(), cnt + 1);
    }
}
