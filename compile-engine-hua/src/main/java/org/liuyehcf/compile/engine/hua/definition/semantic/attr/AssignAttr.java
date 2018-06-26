package org.liuyehcf.compile.engine.hua.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 属性赋值
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class AssignAttr extends AbstractSemanticAction implements Serializable {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 源属性-名称
     */
    private final AttrName fromAttrName;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int toStackOffset;

    /**
     * 宿属性-名称
     */
    private final AttrName toAttrName;

    public AssignAttr(int fromStackOffset, int toStackOffset, AttrName attrName) {
        this(fromStackOffset, attrName, toStackOffset, attrName);
    }

    public AssignAttr(int fromStackOffset, AttrName fromAttrName, int toStackOffset, AttrName toAttrName) {
        this.fromStackOffset = fromStackOffset;
        this.fromAttrName = fromAttrName;
        this.toStackOffset = toStackOffset;
        this.toAttrName = toAttrName;
    }

    @Override
    public void onAction(HuaContext context) {
        Object fromAttrValue = context.getAttr(fromStackOffset, fromAttrName);

        context.setAttr(toStackOffset, toAttrName, fromAttrValue);
    }
}
