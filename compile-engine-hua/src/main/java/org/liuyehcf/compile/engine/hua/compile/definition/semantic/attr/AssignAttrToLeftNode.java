package org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 复制属性给产生式左部的节点
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class AssignAttrToLeftNode extends AbstractSemanticAction {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 属性-名称
     */
    private final AttrName attrName;

    public AssignAttrToLeftNode(int fromStackOffset, AttrName attrName) {
        this.fromStackOffset = fromStackOffset;
        this.attrName = attrName;
    }

    @Override
    public void onAction(HuaContext context) {
        Object value = context.getAttr(fromStackOffset, attrName);
        context.setAttrToLeftNode(attrName, value);
    }
}
