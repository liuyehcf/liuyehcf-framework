package org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 必要时添加布尔表达式属性（特指BOOLEAN_EXPRESSION_TYPE）
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class SetBooleanAttrIfNecessary extends AbstractSemanticAction {

    /**
     * 表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public SetBooleanAttrIfNecessary(int expressionStackOffset) {
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type type = context.getAttr(expressionStackOffset, AttrName.TYPE);
        if (!Type.TYPE_BOOLEAN.equals(type)) {
            return;
        }
        ControlTransferType controlTransferType = context.getAttr(expressionStackOffset, AttrName.CONTROL_TRANSFER_TYPE);
        if (controlTransferType == null) {
            context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFEQ);
        }
    }
}
