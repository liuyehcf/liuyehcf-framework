package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * 设置综合属性，来源于系统
 *
 * @author chenlu
 * @date 2018/6/2
 */
public class SetAttrFromSystem extends AbstractSemanticAction {
    /**
     * 属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    /**
     * 属性-名称
     */
    private final String attrName;

    /**
     * 属性-值
     */
    private final Object attrValue;

    public SetAttrFromSystem(int stackOffset, String attrName, Object attrValue) {
        this.stackOffset = stackOffset;
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getStack().get(stackOffset).put(attrName, attrValue);
    }
}
