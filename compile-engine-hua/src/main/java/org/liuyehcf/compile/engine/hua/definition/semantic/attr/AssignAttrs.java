package org.liuyehcf.compile.engine.hua.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNull;

/**
 * 属性赋值
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class AssignAttrs extends AbstractSemanticAction implements Serializable {

    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int toStackOffset;

    /**
     * 属性名称集合
     */
    private final AttrName[] attrNames;

    public AssignAttrs(int fromStackOffset, int toStackOffset, AttrName... attrNames) {
        this.fromStackOffset = fromStackOffset;
        this.toStackOffset = toStackOffset;
        this.attrNames = attrNames;
    }

    @Override
    public void onAction(HuaContext context) {
        for (AttrName attrName : attrNames) {
            Object value = context.getAttr(fromStackOffset, attrName);
            if (value == null) {
                continue;
            }

            Object origin = context.getAttr(toStackOffset, attrName);
            assertNull(origin);
            context.setAttr(toStackOffset, attrName, value);
        }
    }
}
